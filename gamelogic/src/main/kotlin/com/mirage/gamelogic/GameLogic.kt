package com.mirage.gamelogic

import com.mirage.utils.messaging.ClientMessage

/** Game logic facade */
interface GameLogic {

    /** Starts game logic loop */
    fun startLogic()

    /**
     * Creates a new entity for a player at the next tick of game loop and invokes [onComplete]
     * with new entity's ID as a parameter.
     * [onComplete] is invoked at game loop thread.
     * //TODO Передавать информацию о скиллах, экипировке и т.д. игрока
     */
    fun addNewPlayer(onComplete: (playerID: Long) -> Unit)

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