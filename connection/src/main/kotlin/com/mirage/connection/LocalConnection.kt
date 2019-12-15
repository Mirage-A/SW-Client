package com.mirage.connection

import com.mirage.gamelogic.GameLogic
import com.mirage.gamelogic.GameLogicImpl
import com.mirage.utils.Log
import com.mirage.utils.messaging.*

/** [Connection] implementation which works with local game logic (singleplayer game) */
class LocalConnection(private val mapName: String, private val serverMessageListener: (ServerMessage) -> Unit) : Connection {

    @Volatile
    private var playerID : Long? = null

    @Volatile
    private var stateWithPlayerIDReceived = false

    private val logic : GameLogic = GameLogicImpl(mapName, {
        if (stateWithPlayerIDReceived) serverMessageListener(it)
    })
    { initialObjs, timeMillis ->
        val id = playerID
        if (!stateWithPlayerIDReceived && id != null) {
            println("Initial state! $initialObjs $timeMillis")
            serverMessageListener(InitialGameStateMessage(mapName, initialObjs, id, timeMillis))
            stateWithPlayerIDReceived = true
        }
    }


    /**
     * Запускает локальную логику
     * Вызов этого метода блокирует поток, пока не будет получено первое состояние
     */
    override fun start() {
        println("connection.start() invoked")
        logic.addNewPlayer {
            synchronized(this) {
                playerID = it
                println("PlayerID received! $playerID")
            }
        }
        logic.startLogic()
        while (!stateWithPlayerIDReceived) {Thread.sleep(10L)}
        println("connection.start() finished")
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