package com.mirage.core.messaging.streams

import com.mirage.core.messaging.ServerMessage

/**
 * [ServerMessage] reader.
 * Used at client side.
 */
interface ServerMessageReader {

    /** Wait for a new message and read it.
     * @return null if a message is incorrect.
     */
    fun read() : ServerMessage?

}