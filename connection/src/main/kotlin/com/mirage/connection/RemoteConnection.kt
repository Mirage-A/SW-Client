package com.mirage.connection

import com.mirage.core.utils.IntervalMillis
import com.mirage.core.messaging.ClientMessage
import com.mirage.core.messaging.ServerMessage

/** [Connection] implementation which works with remote server (multiplayer game) */
class RemoteConnection : Connection {

    override fun start() = TODO("not implemented")

    override fun close() = TODO("not implemented")

    override fun sendMessage(msg: ClientMessage): Unit = TODO("not implemented")

    private fun sendAndFlush(msg: ClientMessage): Unit = TODO("not implemented")

    override fun forNewMessages(maxTime: IntervalMillis, block: (ServerMessage) -> Unit) = TODO("not implemented")

}