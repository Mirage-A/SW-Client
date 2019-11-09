package com.mirage.connection

import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import rx.Observable

interface Connection {

    /**
     * [Observable], который позволяет следить за получаемыми от логики сообщениями.
     */
    val serverMessages: Observable<ServerMessage>

    /**
     * Подключается к логике.
     * До вызова этого метода рекомендуется подписаться на [serverMessages]
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