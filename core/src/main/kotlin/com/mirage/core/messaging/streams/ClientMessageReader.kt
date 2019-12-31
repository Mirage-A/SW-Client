package com.mirage.core.messaging.streams

import com.mirage.core.messaging.ClientMessage

/**
 * [ClientMessage] reader.
 * Is used at server side.
 */
interface ClientMessageReader {

    /**
     * Wait for a new message and read it
     * @return null if a message is incorrect
     */
    fun read(): ClientMessage?

}