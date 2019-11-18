package com.mirage.utils.messaging.streams

import com.mirage.utils.messaging.ClientMessage

/**
 * Интерфейс, позволяющий отправлять сообщения от клиента.
 * Используется на стороне клиента.
 */
interface ClientMessageWriter {

    /**
     * Подготовить сообщение к отправке
     * @see [flush]
     */
    fun write(msg: ClientMessage)

    /**
     * Отправить все сообщения
     * @see [write]
     */
    fun flush()
}