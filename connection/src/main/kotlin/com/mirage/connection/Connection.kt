package com.mirage.connection

import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import rx.Observable

interface Connection {

    /** Open connection to game logic */
    fun start()

    /** Send message [msg] to game logic */
    fun sendMessage(msg: ClientMessage)

    /** Closes connection to logic. This connection must not be used anymore */
    fun close()


}