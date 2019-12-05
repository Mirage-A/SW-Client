package com.mirage.utils.messaging.streams

import com.mirage.utils.messaging.ClientMessage

/**
 * [ClientMessage] reader.
 * Is used at server side.
 */
interface ClientMessageReader {

    /**
     * Wait for a new message and read it
     * @return null if a message is incorrect
     */
    fun read() : ClientMessage?

}