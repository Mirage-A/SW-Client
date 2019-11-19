package com.mirage.utils.messaging.streams

import com.mirage.utils.messaging.ServerMessage

/**
 * Интерфейс, позволяющий считывать сообщения от логики.
 * Используется на стороне клиента.
 */
interface ServerMessageReader {

    /**
     * Дождаться следующего сообщения и прочитать его
     */
    fun read() : ServerMessage?

}