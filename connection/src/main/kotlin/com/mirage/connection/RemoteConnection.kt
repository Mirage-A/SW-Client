package com.mirage.connection

import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage

/** [Connection] implementation which works with remote server (multiplayer game) */
class RemoteConnection : Connection {

    override fun start() = TODO("not implemented")

    override fun close() = TODO("not implemented")

    override fun sendMessage(msg: ClientMessage) : Unit = TODO("not implemented")

    private fun sendAndFlush(msg: ClientMessage) : Unit = TODO("not implemented")

    override fun forNewMessages(block: (ServerMessage) -> Unit) = TODO("not implemented")

}