package com.mirage.gamelogic

import com.mirage.utils.extensions.EntityID
import com.mirage.utils.extensions.PlayerCreationListener
import com.mirage.utils.extensions.QuestProgress
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import java.util.*

/** Game logic facade */
interface GameLogic {

    /**
     * Concurrent queue with pairs of a message and id of an entity of a player who must receive this message.
     * If id is equal to -1, then message must be sent to all players in this room.
     */
    val serverMessages: Queue<Pair<EntityID, ServerMessage>>

    /** Concurrent queue with requests to transfer player with given ID to another room with given map name */
    val playerTransfers: Queue<PlayerTransferRequest>

    /** Starts game logic loop */
    fun startLogic()

    /**
     * Creates a new entity for a player at the next tick of game loop and invokes [onComplete]
     * with new entity's ID as a parameter.
     * [onComplete] is invoked at game loop thread.
     */
    fun addNewPlayer(globalQuestProgress: QuestProgress? = null, onComplete: PlayerCreationListener)

    /** Removes player's entity from scene */
    fun removePlayer(playerID: EntityID)

    /** Pauses game loop. It can be resumed by [resumeLogic] */
    fun pauseLogic()

    /** Resumes game loop. This method should be invoked only after [pauseLogic] */
    fun resumeLogic()

    /** Fully stops game loop. It can't be restarted anymore. */
    fun stopLogic()

    /** Fully stops game loop. It can't be restarted anymore. */
    fun dispose()

    /**
     * Adds message [msg] to message queue.
     * This message will be handled at the next tick of game loop.
     * [id] - ID of an entity bound to player who sent this message (not id of a player itself).
     */
    fun handleMessage(id: Long, msg: ClientMessage)

}