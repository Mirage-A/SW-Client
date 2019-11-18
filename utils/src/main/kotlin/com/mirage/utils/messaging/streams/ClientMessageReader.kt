package com.mirage.utils.messaging.streams

import com.mirage.utils.messaging.ClientMessage

/**
 * Интерфейс, позволяющий считывать сообщения от клиента.
 * Используется на стороне логики (сервера).
 */
interface ClientMessageReader {

    /**
     * Дождаться нового сообщения и прочесть его
     * @throws Exception если сообщение некорректно (например, при десериализации)
     */
    fun read() : ClientMessage

}