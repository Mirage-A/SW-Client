package com.mirage.gamelogic

import com.mirage.utils.extensions.PlayerCreationListener
import com.mirage.utils.extensions.QuestProgress
import com.mirage.utils.messaging.ClientMessage

internal interface GameLoop {

    fun start()

    fun addNewPlayer(globalQuestProgress: QuestProgress?, onComplete: PlayerCreationListener)

    fun pause()

    fun resume()

    fun stop()

    fun dispose()

    /**
     * Adds message [msg] in the message queue.
     * [id] - ID of the player's entity (it's id of ENTITY, not a PLAYER server-wise)
     */
    fun handleMessage(id: Long, msg: ClientMessage)

}