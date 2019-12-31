package com.mirage.logic

import com.mirage.core.utils.EntityID
import com.mirage.core.utils.PlayerCreationRequest
import com.mirage.core.messaging.ClientMessage
import com.mirage.core.messaging.ServerMessage
import com.mirage.logic.data.PlayerTransferRequest
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
    fun startLogic(initialPlayerRequests: Iterable<PlayerCreationRequest> = ArrayList())

    /**
     * Creates a new entity for a player at the next tick of game loop and invokes [PlayerCreationRequest.onCreate]
     * with new entity's ID as a parameter.
     * [PlayerCreationRequest.onCreate] is invoked at game loop thread.
     */
    fun addNewPlayer(request: PlayerCreationRequest)

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