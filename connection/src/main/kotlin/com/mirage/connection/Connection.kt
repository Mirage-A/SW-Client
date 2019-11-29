package com.mirage.connection

import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import rx.Observable

interface Connection {

    /**
     * Подключается к логике.
     */
    fun start()

    /**
     * Отправляет сообщение [msg] логике.
     */
    fun sendMessage(msg: ClientMessage)

    /**
     * Полностью останавливает взаимодействие с логикой и освобождает все ресурсы
     */
    fun close()


}