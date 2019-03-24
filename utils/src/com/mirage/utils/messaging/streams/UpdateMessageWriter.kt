package com.mirage.utils.messaging.streams

import com.mirage.utils.messaging.UpdateMessage

/**
 * Интерфейс, позволяющий отправлять сообщения от логики.
 * Используется на стороне логики (сервера).
 */
interface UpdateMessageWriter {

    /**
     * Отправить сообщение
     */
    fun write(msg: UpdateMessage)

}