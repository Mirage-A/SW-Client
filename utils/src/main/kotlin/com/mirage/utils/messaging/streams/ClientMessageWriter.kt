package com.mirage.utils.messaging.streams

import com.mirage.utils.messaging.ClientMessage

/**
 * [ClientMessage] writer.
 * Used at client side.
 */
interface ClientMessageWriter {

    /** Writes a message. Use [flush] to send all written messages */
    fun write(msg: ClientMessage)

    /** Send all messages written by [write] */
    fun flush()
}