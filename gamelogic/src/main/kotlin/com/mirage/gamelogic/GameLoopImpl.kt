package com.mirage.gamelogic

import com.mirage.utils.GAME_LOOP_TICK_INTERVAL
import com.mirage.utils.Log
import com.mirage.utils.LoopTimer
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.maps.SceneLoader
import com.mirage.utils.game.states.ExtendedState
import com.mirage.utils.game.states.SimplifiedState
import com.mirage.utils.game.states.StateDifference
import com.mirage.utils.messaging.*
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.collections.ArrayList

internal class GameLoopImpl(gameMapName: String,
                            private val serverMessageListener: (ServerMessage) -> Unit,
                            private val stateUpdateListener: (SimplifiedState, Long) -> Unit) : GameLoop {


    /**
     * Observable, в котором хранятся получаемые сообщения от клиентов.
     * Первый аргумент пары - id персонажа игрока (не самого игрока, а его персонажа на сцене).
     */
    private val clientMessages : Queue<Pair<Long, ClientMessage>> = ConcurrentLinkedQueue()


    private val gameMap : GameMap = SceneLoader.loadMap(gameMapName)
    private val state: ExtendedState = SceneLoader.loadInitialState(gameMapName)
    private var latestStateSnapshot: SimplifiedState = state.simplifiedDeepCopy()

    /**
     * Функция, которая обновляет состояние игры, вызывая [serverMessageListener] для сообщений.
     * @param delta Время в миллисекундах, прошедшее с момента последнего вызова этой функции.
     */
    private fun updateState(delta: Long, gameMap: GameMap) {
        if (delta > 200L) Log.i("Slow update: $delta ms")

        val newClientMessages = ArrayList<Pair<Long, ClientMessage>>()
        while (true) {
            val msg = clientMessages.poll()
            msg ?: break
            newClientMessages.add(msg)
        }

        val serverMessages = ArrayDeque<ServerMessage>()
        updateState(delta, state, gameMap, newClientMessages, serverMessages)


        //TODO Добавление игроков

        while (true) {
            val request = newPlayerRequests.poll() ?: break
            val player = createPlayer(gameMap)
            val id = state.addEntity(player)
            request(id)
        }

        val finalState = state.simplifiedDeepCopy()
        val finalDifference = StateDifference(latestStateSnapshot, finalState)
        latestStateSnapshot = finalState
        val time = System.currentTimeMillis()
        serverMessageListener(GameStateUpdateMessage(finalDifference, time))
        for (msg in serverMessages) {
            serverMessageListener(msg)
        }
        stateUpdateListener(finalState, time)
    }

    /**
     * Очередь, в которой хранятся запросы на создание персонажа игрока.
     * При создании игрока вызывается слушатель из очереди.
     */
    private val newPlayerRequests : Queue<(Long) -> Unit> = ConcurrentLinkedQueue()

    override fun addNewPlayer(onComplete: (playerID: Long) -> Unit) {
        newPlayerRequests.add(onComplete)
    }



    private val loopTimer = LoopTimer(GAME_LOOP_TICK_INTERVAL) {
        updateState(it, gameMap)
    }

    override fun start() {
        println("Starting logic thread....")
        stateUpdateListener(latestStateSnapshot, System.currentTimeMillis())
        loopTimer.start()
    }

    override fun pause() = loopTimer.pause()

    override fun resume() = loopTimer.resume()

    override fun stop() = loopTimer.stop()

    override fun dispose() {
        stop()
        //TODO
    }

    override fun handleMessage(id: Long, msg: ClientMessage) {
        clientMessages.add(Pair(id, msg))
    }
}