package com.mirage.server

import com.badlogic.gdx.net.Socket
import com.mirage.utils.SERVER_MESSAGE_BUFFER_UPDATE_INTERVAL
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.MoveDirection
import com.mirage.utils.messaging.UpdateMessage
import com.mirage.utils.messaging.streams.impls.ClientMessageInputStream
import com.mirage.utils.messaging.streams.impls.UpdateMessageOutputStream
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Конкретный игрок (не зависим от комнаты)
 * Содержит потоки, работающие с сокетом с игроком
 * Обрабатывает поток сообщений в обе стороны
 * //TODO Убираем потоки, переходим на Netty
 * //TODO Как-нибудь надо справляться с дудосом (спамом юзлесс сообщениями)
 */
class Player(private val socket: Socket,
             private val msgListener : (ClientMessage) -> Unit,
             private val disconnectListener: (Player) -> Unit) {

    var room: Room? = null
    var id: Long? = null

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

    private val inJob = Thread(Runnable {
        try {
            while (true) {
                inputMessageQueue.add(inStream.read())
            }
        }
        catch(ex: InterruptedException) {}
        catch(ex: Exception) {
            println("Deserialization exception")
            disconnectListener(this@Player)
            kill()
        }
    })

    private val outJob = Thread(Runnable {
        try {
            while (true) {
                while (outputMessageQueue.isNotEmpty()) {
                    outStream.write(outputMessageQueue.poll())
                }
                Thread.sleep(SERVER_MESSAGE_BUFFER_UPDATE_INTERVAL)
            }
        }
        catch(ex: InterruptedException) {}
    })

    init {
        inJob.start()
        outJob.start()
    }

    fun kill() {
        inJob.interrupt()
        outJob.interrupt()
        socket.dispose()
    }
}