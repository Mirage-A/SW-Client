package com.mirage.utils.messaging.streams

import com.mirage.utils.messaging.ServerMessage


/**
 * [ServerMessage] writer.
 * Used by server side.
 */
interface ServerMessageWriter {

    /** Writes a message. Use [flush] to send all written messages. */
    fun write(msg: ServerMessage)

    /** Sends all messages written by [write] */
    fun flush()

}