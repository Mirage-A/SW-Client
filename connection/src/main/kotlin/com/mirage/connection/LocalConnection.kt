package com.mirage.connection

import com.mirage.gamelogic.GameLogic
import com.mirage.gamelogic.GameLogicImpl
import com.mirage.utils.Log
import com.mirage.utils.extensions.EntityID
import com.mirage.utils.extensions.GameMapName
import com.mirage.utils.extensions.QuestProgress
import com.mirage.utils.extensions.mutableCopy
import com.mirage.utils.game.states.SimplifiedState
import com.mirage.utils.messaging.*
import com.mirage.utils.preferences.Prefs
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/** [Connection] implementation which works with local game logic (single player game) */
class LocalConnection(private val mapName: GameMapName) : Connection {

    @Volatile
    private var playerID : EntityID? = null

    /** A message we want to send to client before all server messages */
    @Volatile
    private var firstStateWithPlayerMessage: InitialGameStateMessage? = null

    private val logic : GameLogic = GameLogicImpl(mapName)

    override fun start() {
        Log.i("connection.start() invoked")
        val lock = ReentrantLock()
        val idReceivedCondition = lock.newCondition()
        logic.addNewPlayer(QuestProgress(Prefs.profile.globalQuestProgress)) {
            lock.withLock {
                playerID = it
                Log.i("PlayerID received! $playerID")
                idReceivedCondition.signal()
            }
        }
        logic.startLogic()
        lock.withLock {
            idReceivedCondition.await()
        }
        var currentState: SimplifiedState? = null
        while (true) {
            val msg = logic.serverMessages.poll()?.second
            if (msg == null) {
                Thread.sleep(10L)
            }
            else if (msg is InitialGameStateMessage) {
                currentState = msg.initialState
            }
            else if (msg is GameStateUpdateMessage) {
                if (currentState != null) currentState = msg.diff.projectOn(currentState)
                if (playerID != null) {
                    firstStateWithPlayerMessage = InitialGameStateMessage(
                            mapName, currentState ?: SimplifiedState(), playerID!!, msg.stateCreatedTimeMillis
                    )
                    break
                }
            }
        }
        Log.i("connection.start() finished")
    }

    override fun sendMessage(msg: ClientMessage) {
        playerID?.let {
            logic.handleMessage(it, msg)
        }
        if (playerID == null) {
            Log.e("PlayerID is null, invoke connection.start() before sending any messages.")
        }
    }

    override fun forNewMessages(block: (ServerMessage) -> Unit) {
        //TODO Process transfers to another map
        if (firstStateWithPlayerMessage != null) {
            block(firstStateWithPlayerMessage!!)
            firstStateWithPlayerMessage = null
        }
        while (true) {
            val (id, msg) = logic.serverMessages.poll() ?: break
            if (id == -1L || id == playerID) block(msg)
        }
    }

    override fun close() {
        logic.stopLogic()
        logic.dispose()
    }

}