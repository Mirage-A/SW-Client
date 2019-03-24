package com.mirage.server

import com.badlogic.gdx.net.Socket
import com.mirage.utils.SERVER_MESSAGE_BUFFER_UPDATE_INTERVAL
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.UpdateMessage
import com.mirage.utils.messaging.streams.impls.ClientMessageInputStream
import com.mirage.utils.messaging.streams.impls.UpdateMessageOutputStream
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Конкретный игрок (не зависим от комнаты)
 * Содержит корутину, работающую с сокетом с игроком
 * Обрабатывает поток сообщений в обе стороны
 */
class Player(private val socket: Socket,
             private val msgListener : (ClientMessage) -> Unit,
             private val disconnectListener: () -> Unit) {

    fun checkNewMessages() {
        while (inputMessageQueue.isNotEmpty()) {
            msgListener(inputMessageQueue.poll())
        }
    }

    fun sendMessage(msg: UpdateMessage) {
        outputMessageQueue.add(msg)
    }

    private val outputMessageQueue = ConcurrentLinkedQueue<UpdateMessage>()

    private val inputMessageQueue = ConcurrentLinkedQueue<ClientMessage>()

    private val inStream = ClientMessageInputStream(socket.inputStream)

    private val outStream = UpdateMessageOutputStream(socket.outputStream)

    private val inJob = GlobalScope.launch {
        try {
            while (true) {
                inputMessageQueue.add(inStream.read())
            }
        }
        catch(ex: Exception) {
            println("Deserialization exception")
            disconnectListener()
            kill()
        }
    }

    private val outJob = GlobalScope.launch {
        while(true) {
            while (outputMessageQueue.isNotEmpty()) {
                outStream.write(outputMessageQueue.poll())
            }
            delay(SERVER_MESSAGE_BUFFER_UPDATE_INTERVAL)
        }
    }

    fun kill() {
        inJob.cancel()
        outJob.cancel()
        socket.dispose()
    }
}