package com.mirage.gamelogic

import com.mirage.utils.Log
import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.game.maps.*
import com.mirage.utils.game.states.GameStateSnapshot
import com.mirage.utils.game.states.StateDifference
import com.mirage.utils.messaging.ClientMessage
import io.reactivex.subjects.PublishSubject
import rx.subjects.BehaviorSubject
import rx.subjects.Subject
import java.util.concurrent.locks.ReentrantLock
import com.mirage.utils.messaging.EventSubjectAdapter
import com.mirage.utils.messaging.ServerMessage
import com.mirage.utils.messaging.StateDifferenceMessage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

internal class GameLoopImpl(private val gameMapName: String) : GameLoop {

    /**
     * Observable, в который логика скидывает новые состояния игры и время их создания после каждого тика цикла
     */
    override val latestState : Subject<Pair<GameObjects, Long>, Pair<GameObjects, Long>> = BehaviorSubject.create()!!

    /**
     * Observable, в который логика скидывает сообщения, которые должны быть отправлены ВСЕМ клиентам,
     * подключившимся после отправки сообщения.
     */
    override val serverMessages : Subject<ServerMessage, ServerMessage> = EventSubjectAdapter()

    /**
     * Observable, в котором хранятся получаемые сообщения от клиентов.
     * Первый аргумент пары - id персонажа игрока (не самого игрока, а его персонажа на сцене).
     */
    private val clientMessages : Subject<Pair<Long, ClientMessage>, Pair<Long, ClientMessage>> = EventSubjectAdapter()

    @Volatile
    private var isStopped = false

    /**
     * Функция, которая обновляет состояние игры,
     * отправляя сообщение об этом в [latestState] и [serverMessages].
     * @param delta Время в миллисекундах, прошедшее с момента последнего вызова этой функции.
     */
    private tailrec fun update(delta: Long, originState: GameObjects, gameMap: GameMap) {
        println("Logic update $delta ms")
        if (delta > 200L) Log.i("Slow update: $delta ms")

        val startTime = System.currentTimeMillis()
        val msgs = ArrayList<Pair<Long, ClientMessage>>()
        clientMessages.subscribe( {msgs.add(it)}, {println("ERROR $it")} ).unsubscribe()
        val (mutableState, newMessages) = updateState(delta, originState, gameMap, msgs)

        //TODO Добавление игроков

        while (newPlayerRequests.isNotEmpty()) {
            //TODO Заполнение характеристик персонажа игрока, в том числе в зависимости от карты
            val player = createPlayer(gameMap)
            val id = mutableState.add(player)
            val subj = newPlayerRequests.poll()
            subj.onNext(id)
            subj.onComplete()
        }

        val finalChanges = mutableState.findDifferenceWithOrigin()
        val finalState = mutableState.saveChanges()
        latestState.onNext(Pair(finalState, System.currentTimeMillis()))
        serverMessages.onNext(StateDifferenceMessage(finalChanges, System.currentTimeMillis()))
        for (msg in newMessages) {
            serverMessages.onNext(msg)
        }

        updateLock.unlock()
        updateLock.lock()
        if (!isStopped) {
            val timeDiff = System.currentTimeMillis() - startTime
            if (timeDiff < 95L) {
                Thread.sleep(100L - timeDiff)
            }
            val newDelta = min(System.currentTimeMillis() - startTime, 200L)
            update(newDelta, finalState, gameMap)
        }
    }

    private val updateLock = ReentrantLock(true)

    /**
     * Изменяемая небезопасная очередь, в которой хранятся запросы на создание персонажа игрока.
     * При каждом запросе создаётся Subject, на который подписывается этот запрос.
     * После каждого тика логики логика пробегает по этой очереди, добавляет игроков на карту
     * и для каждого Subject-а вызывает onNext от ID добавленного персонажа на карте.
     */
    private val newPlayerRequests : Queue<io.reactivex.subjects.Subject<Long>> = ArrayDeque()

    override fun addNewPlayer(): Long {
        val subj = PublishSubject.create<Long>()
        updateLock.lock()
        newPlayerRequests.add(subj)
        updateLock.unlock()
        return subj.blockingFirst()
    }

    //TODO Возможно, при потере ссылки на поток он может остановиться, но это не точно
    override fun start() {
        println("Starting logic thread....")
        Thread (Runnable{
            println("Logic thread started!")
            val (map, objs) = SceneLoader.loadScene(gameMapName)
            println("Map loaded in logic!")
            updateLock.lock()
            update(0L, objs, map)
        }).start()
    }

    override fun pause() = updateLock.lock()

    override fun resume() = updateLock.unlock()

    override fun stop() {
        updateLock.lock()
        isStopped = true
        updateLock.unlock()
    }

    override fun dispose() {
        stop()
        //TODO
    }

    override fun handleMessage(id: Long, msg: ClientMessage) = clientMessages.onNext(Pair(id, msg))
}