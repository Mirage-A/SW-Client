package com.mirage.utils.messaging.streams

import com.mirage.utils.messaging.ClientMessage

/**
 * Интерфейс, позволяющий считывать сообщения от клиента.
 * Используется на стороне логики (сервера).
 */
interface ClientMessageReader {

    /**
     * Дождаться нового сообщения и прочесть его
     */
    fun read() : ClientMessage

}