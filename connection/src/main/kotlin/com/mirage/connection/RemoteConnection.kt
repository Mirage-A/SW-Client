package com.mirage.connection

import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Реализация интерфейса [Connection], отвечающая за подключение к удаленному серверу.
 */
class RemoteConnection : Connection {

    override val serverMessages: Observable<ServerMessage> = PublishSubject.create()

    override fun start() = TODO("not implemented")

    override fun close() = TODO("not implemented")

    /**
     * Отправляет сообщение [msg] на сервер
     */
    override fun sendMessage(msg: ClientMessage) : Unit = TODO("not implemented")

    private fun sendAndFlush(msg: ClientMessage) : Unit = TODO("not implemented")


}