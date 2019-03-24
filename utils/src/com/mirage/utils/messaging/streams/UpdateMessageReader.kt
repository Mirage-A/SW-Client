package com.mirage.utils.messaging.streams

import com.mirage.utils.messaging.UpdateMessage

/**
 * Интерфейс, позволяющий считывать сообщения от логики.
 * Используется на стороне клиента.
 */
interface UpdateMessageReader {

    /**
     * Дождаться следующего сообщения и прочитать его
     */
    fun read() : UpdateMessage

}