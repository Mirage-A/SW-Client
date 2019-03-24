package com.mirage.server

import com.badlogic.gdx.net.Socket
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.UpdateMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Конкретный игрок (не зависим от комнаты)
 * Содержит корутину, работающую с сокетом с игроком
 * Обрабатывает поток сообщений в обе стороны
 */
class Player(private val socket: Socket) {

    val outputMessageQueue = ConcurrentLinkedQueue<UpdateMessage>()

    val inputMessageQueue = ConcurrentLinkedQueue<ClientMessage>()

    private var killed = false

    private val job = GlobalScope.launch {
        while(!killed) {
            println("${socket.isConnected} messaging with player ${socket.remoteAddress}")
            //TODO Обработка дисконнекта
            delay(1000)
        }
    }

    fun kill() {
        killed = true
        job.cancel()
        socket.dispose()
    }
}