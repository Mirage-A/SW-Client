package com.mirage.connection

import com.mirage.gamelogic.GameLogic
import com.mirage.gamelogic.GameLogicImpl
import com.mirage.utils.Log
import com.mirage.utils.messaging.*
import rx.subjects.Subject

/**
 * Реализация интерфейса Connection, работающая с локальным сервером (одиночная игра)
 */
class LocalConnection(private val mapName: String) : Connection {

    private val logic : GameLogic = GameLogicImpl(mapName)

    override val serverMessages: Subject<ServerMessage, ServerMessage> = EventSubjectAdapter()

    private var playerID : Long? = null

    /**
     * Запускает локальную логику
     * Вызов этого метода блокирует поток, пока не будет получено первое состояние
     */
    override fun start() {
        logic.startLogic()
        playerID = logic.addNewPlayer()
        val initialState = logic.latestState.toBlocking().first()
        val initialMsg = InitialGameStateMessage(mapName, initialState.first, playerID!!, initialState.second)
        println("got initial state from logic! $initialMsg")
        serverMessages.onNext(initialMsg)
        logic.serverMessages.subscribe {
            println("got msg from logic in connection! $it")
            serverMessages.onNext(it)
        }
    }

    override fun sendMessage(msg: ClientMessage) {
        playerID?.let {
            logic.handleMessage(it, msg)
        }
        if (playerID == null) {
            Log.e("PlayerID is null, invoke connection.start() before sending any messages.")
        }
    }

    override fun close() {
        logic.stopLogic()
        logic.dispose()
    }

}