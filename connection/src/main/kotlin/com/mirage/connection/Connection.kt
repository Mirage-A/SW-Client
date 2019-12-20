package com.mirage.connection

import com.mirage.utils.extensions.IntervalMillis
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage

interface Connection {

    /** Open connection to game logic */
    fun start()

    /** Send message [msg] to game logic */
    fun sendMessage(msg: ClientMessage)

    /** Invokes [block] for new messages.
     *  Every message will be processed only 1 time during all [forNewMessages] invocations.
     *  [block] is invoked in the same thread as this function.
     *  Function stops processing new messages after [maximumProcessingTime] even if some messages are left
     *  */
    fun forNewMessages(maximumProcessingTime: IntervalMillis = Long.MAX_VALUE, block: (ServerMessage) -> Unit)

    /** Closes connection to logic. This connection must not be used anymore */
    fun close()


}