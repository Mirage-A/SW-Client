package com.mirage.connection

import com.mirage.core.messaging.ClientMessage
import com.mirage.core.messaging.GameStateUpdateMessage
import com.mirage.core.messaging.InitialGameStateMessage
import com.mirage.core.messaging.ServerMessage
import com.mirage.core.preferences.Prefs
import com.mirage.core.utils.*
import com.mirage.logic.GameLogic
import com.mirage.logic.GameLogicImpl
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/** [Connection] implementation which works with local game logic (single player game) */
class LocalConnection(private val mapName: GameMapName) : Connection {

    @Volatile
    private var playerID: EntityID? = null

    /** Messages received before initial state with player */
    private val bufferedMessages = ArrayDeque<ServerMessage>()

    private val logic: GameLogic = GameLogicImpl(mapName)

    override fun start() {
        Log.i("connection.start() invoked")
        val lock = ReentrantLock()
        val idReceivedCondition = lock.newCondition()
        val questProgress = QuestProgress(Prefs.profile.globalQuestProgress)
        val newPlayerListener: PlayerCreationListener = {
            lock.withLock {
                playerID = it
                Log.i("PlayerID received! $playerID")
                idReceivedCondition.signal()
            }
        }
        logic.startLogic(listOf(PlayerCreationRequest(Prefs.profile.profileName, questProgress, Prefs.profile.currentEquipment, newPlayerListener)))
        lock.withLock {
            idReceivedCondition.await()
        }
        var initialStateReceived = false
        while (true) {
            val msg = logic.serverMessages.poll()?.second
            if (msg == null) {
                Thread.sleep(10L)
            } else {
                if (!initialStateReceived && (msg !is InitialGameStateMessage || msg.playerID == -1L)) continue
                initialStateReceived = true
                bufferedMessages.add(msg)
                val receivedID = playerID
                if (msg is GameStateUpdateMessage && receivedID != null) {
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

    override fun forNewMessages(maximumProcessingTime: IntervalMillis, block: (ServerMessage) -> Unit) {
        //TODO Process transfers to another map
        if (bufferedMessages.isNotEmpty()) {
            for (msg in bufferedMessages) {
                block(msg)
            }
            bufferedMessages.clear()
        }
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime <= maximumProcessingTime) {
            val (id, msg) = logic.serverMessages.poll() ?: break
            if (id == -1L || id == playerID) block(msg)
        }
    }

    private var isClosed = false

    override fun close() {
        if (!isClosed) {
            logic.stopLogic()
            logic.dispose()
            isClosed = true
        }
    }

}