package com.mirage.gamelogic

import com.mirage.utils.GAME_LOOP_TICK_INTERVAL
import com.mirage.utils.Log
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.maps.SceneLoader
import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.messaging.*
import java.lang.Exception
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.ArrayList

internal class GameLoopImpl(gameMapName: String,
                            private val serverMessageListener: (ServerMessage) -> Unit,
                            private val stateUpdateListener: (GameObjects, Long) -> Unit) : GameLoop {


    /**
     * Observable, в котором хранятся получаемые сообщения от клиентов.
     * Первый аргумент пары - id персонажа игрока (не самого игрока, а его персонажа на сцене).
     */
    private val clientMessages : Queue<Pair<Long, ClientMessage>> = ConcurrentLinkedQueue()


    /**
     * Функция, которая обновляет состояние игры,
     * отправляя сообщение об этом в [latestState] и [serverMessages].
     * @param delta Время в миллисекундах, прошедшее с момента последнего вызова этой функции.
     */
    private fun update(delta: Long, originState: GameObjects, gameMap: GameMap) : GameObjects {
        if (delta > 200L) Log.i("Slow update: $delta ms")

        val msgs = ArrayList<Pair<Long, ClientMessage>>()

        while (true) {
            val msg = clientMessages.poll()
            msg ?: break
            println("Processing client message $msg")
            msgs.add(msg)
        }

        val (mutableState, newMessages) = updateState(delta, originState, gameMap, msgs)

        //TODO Добавление игроков

        while (true) {
            val request = newPlayerRequests.poll() ?: break
            val player = createPlayer(gameMap)
            val id = mutableState.add(player)
            request(id)
        }

        val finalChanges = mutableState.findDifferenceWithOrigin()
        val finalState = mutableState.saveChanges()
        val time = System.currentTimeMillis()
        serverMessageListener(GameStateUpdateMessage(finalChanges, time))
        for (msg in newMessages) {
            serverMessageListener(msg)
        }
        stateUpdateListener(finalState, time)
        return finalState
    }

    /**
     * Очередь, в которой хранятся запросы на создание персонажа игрока.
     * При создании игрока вызывается слушатель из очереди.
     */
    private val newPlayerRequests : Queue<(Long) -> Unit> = ConcurrentLinkedQueue()

    override fun addNewPlayer(onComplete: (playerID: Long) -> Unit) {
        newPlayerRequests.add(onComplete)
    }

    private val initialScene = SceneLoader.loadScene(gameMapName)
    private val gameMap : GameMap = initialScene.first
    //Последнее состояние, полученное вызовом update.
    //Не следует использовать это поле для получения последнего актуального состояния.
    private var lastUpdatedState: GameObjects = initialScene.second

    @Volatile
    private var lastTickTime: Long = System.currentTimeMillis()

    private val lock : Lock = ReentrantLock(true)

    private val timerTask: TimerTask = object: TimerTask() {
        override fun run() {
            lock.lock()
            val time = System.currentTimeMillis()
            lastUpdatedState = update(time - lastTickTime, lastUpdatedState, gameMap)
            lastTickTime = time
            lock.unlock()
        }
    }

    private val loopTimer = Timer(false)

    override fun start() {
        println("Starting logic thread....")
        stateUpdateListener(initialScene.second, System.currentTimeMillis())
        loopTimer.scheduleAtFixedRate(timerTask, 0L, GAME_LOOP_TICK_INTERVAL)
    }

    override fun pause() = lock.lock()

    override fun resume() {
        lastTickTime = System.currentTimeMillis()
        lock.unlock()
    }

    override fun stop() {
        try {
            lock.unlock()
        }
        catch(ex: Exception) {}
        lock.lock()
        loopTimer.cancel()
        lock.unlock()
    }

    override fun dispose() {
        stop()
        //TODO
    }

    override fun handleMessage(id: Long, msg: ClientMessage) {
        clientMessages.add(Pair(id, msg))
    }
}