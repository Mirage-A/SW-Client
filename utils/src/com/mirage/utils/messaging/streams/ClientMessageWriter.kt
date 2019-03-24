package com.mirage.utils.messaging.streams

import com.mirage.utils.messaging.ClientMessage

/**
 * Интерфейс, позволяющий отправлять сообщения от клиента.
 * Используется на стороне клиента.
 */
interface ClientMessageWriter {

    /**
     * Отправить сообщение
     */
    fun write(msg: ClientMessage)

}