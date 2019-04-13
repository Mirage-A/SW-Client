package com.mirage.gamelogic

import com.mirage.utils.maps.*
import com.mirage.utils.messaging.ClientMessage
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
     */
    private val clientMessages : Subject<ClientMessage, ClientMessage> = EventSubjectAdapter()

    @Volatile
    private var isStopped = false

    /**
     * Функция, которая обновляет состояние игры,
     * отправляя сообщение об этом в [observable].
     * @param delta Время в миллисекундах, прошедшее с момента последнего вызова этой функции.
     */
    private tailrec fun update(delta: Long, originState: GameObjects, gameMap: GameMap) {
        val startTime = System.currentTimeMillis()
        val msgs = ArrayList<ClientMessage>()
        clientMessages.subscribe( {msgs.add(it)}, {println("ERROR $it")} ).unsubscribe()
        val changes = updateState(delta, originState, gameMap, msgs)
        observable.onNext(GameStateSnapshot(originState, changes))
        updateLock.unlock()
        updateLock.lock()
        if (!isStopped) {
            val timeDiff = System.currentTimeMillis()
            if (timeDiff < 95L) {
                Thread.sleep(100L - timeDiff)
            }
            val newDelta = Math.min(System.currentTimeMillis() - startTime, 200L)
            update(newDelta, changes.projectOn(originState), gameMap)
        }
    }

    private val updateLock = ReentrantLock()

    override fun start() = Thread {
        updateLock.lock()
        val (map, objs) = SceneLoader.loadScene(gameMapName)
        update(0L, objs, map)
    }.start()

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

    override fun handleMessage(msg: ClientMessage) = clientMessages.onNext(msg)
}