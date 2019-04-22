package com.mirage.gamelogic

import com.mirage.utils.gameobjects.Entity
import com.mirage.utils.gameobjects.GameObject
import com.mirage.utils.gameobjects.GameObjects
import com.mirage.utils.maps.*
import com.mirage.utils.messaging.ClientMessage
import io.reactivex.subjects.PublishSubject
import rx.subjects.BehaviorSubject
import rx.subjects.Subject
import java.util.concurrent.locks.ReentrantLock

internal class GameLoopImpl(private val gameMapName: String) : GameLoop {

    /**
     * Observable, в который логика скидывает новые состояния игры после каждого тика цикла
     */
    override val observable : Subject<GameStateSnapshot, GameStateSnapshot> = BehaviorSubject.create()!!

    /**
     * Observable, в котором хранятся получаемые сообщения от клиентов.
     * Первый аргумент пары - id персонажа игрока (не самого игрока, а его персонажа на сцене).
     */
    private val clientMessages : Subject<Pair<Long, ClientMessage>, Pair<Long, ClientMessage>> = EventSubjectAdapter()

    @Volatile
    private var isStopped = false

    /**
     * Функция, которая обновляет состояние игры,
     * отправляя сообщение об этом в [observable].
     * @param delta Время в миллисекундах, прошедшее с момента последнего вызова этой функции.
     */
    private tailrec fun update(delta: Long, originState: GameObjects, gameMap: GameMap) {
        println("Logic update $delta ms")

        val startTime = System.currentTimeMillis()
        val msgs = ArrayList<Pair<Long, ClientMessage>>()
        clientMessages.subscribe( {msgs.add(it)}, {println("ERROR $it")} ).unsubscribe()
        val changes = updateState(delta, originState, gameMap, msgs)


        val newState = changes.projectOn(originState)
        //TODO Добавление игроков
        //Список, в который будут добавляться персонажи игроков
        val newPlayersMap = HashMap<Long, Entity>()
        //Отслеживаем ID добавляемых игроков
        var counter = newState.nextID
        for (subj in newPlayerRequests) {
            //TODO Заполнение характеристик персонажа игрока, в том числе в зависимости от карты
            newPlayersMap[counter] = createPlayer(gameMap)
            subj.onNext(counter)
            ++counter
            subj.onComplete()
        }

        val finalChanges = StateDifference(
                changes.newObjects + newPlayersMap,
                changes.removedObjects,
                changes.objectDifferences,
                changes.newClientScripts
        )
        val finalState = finalChanges.projectOn(originState)
        observable.onNext(GameStateSnapshot(finalState, finalChanges, System.currentTimeMillis()))

        updateLock.unlock()
        updateLock.lock()
        if (!isStopped) {
            val timeDiff = System.currentTimeMillis() - startTime
            if (timeDiff < 95L) {
                Thread.sleep(100L - timeDiff)
            }
            val newDelta = Math.min(System.currentTimeMillis() - startTime, 200L)
            update(newDelta, finalState, gameMap)
        }
    }

    private val updateLock = ReentrantLock(true)

    /**
     * Изменяемый небезопасный список, в котором хранятся запросы на создание персонажа игрока.
     * При каждом запросе создаётся Subject, на который подписывается этот запрос.
     * После каждого тика логики логика пробегает по этому списку, добавляет игроков на карту
     * и для каждого Subject-а вызывает onNext от ID добавленного персонажа на карте.
     */
    private val newPlayerRequests : MutableList<io.reactivex.subjects.Subject<Long>> = ArrayList()

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