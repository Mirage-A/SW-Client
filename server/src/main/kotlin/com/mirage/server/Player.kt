package com.mirage.server

import com.badlogic.gdx.net.Socket
import com.mirage.utils.SERVER_MESSAGE_BUFFER_UPDATE_INTERVAL
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import com.mirage.utils.messaging.streams.impls.ClientMessageInputStream
import com.mirage.utils.messaging.streams.impls.ServerMessageOutputStream
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Конкретный игрок (не зависим от комнаты)
 * Содержит потоки, работающие с сокетом с игроком
 * Обрабатывает поток сообщений в обе стороны
 * //TODO Убираем потоки, переходим на Netty
 * //TODO Как-нибудь надо справляться с дудосом (спамом юзлесс сообщениями)
 */
class Player(private val socket: Socket,
             private val msgListener : (Player, ClientMessage) -> Unit,
             private val disconnectListener: (Player) -> Unit) {

    var room: Room? = null
    var id: Long? = null

    /**
     * Проверяет наличие новых входящих сообщений и для каждого сообщения вызывает [msgListener]
     */
    fun checkNewMessages() {
        while (inputMessageQueue.isNotEmpty()) {
            msgListener(this, inputMessageQueue.poll())
        }
    }

    /**
     * Добавляет сообщение в очередь на отправку
     * Сообщения из очереди отправляются автоматически
     */
    fun sendMessage(msg: ServerMessage) {
        outputMessageQueue.add(msg)
    }

    private val outputMessageQueue = ConcurrentLinkedQueue<ServerMessage>()

    private val inputMessageQueue = ConcurrentLinkedQueue<ClientMessage>()

    private val inStream = ClientMessageInputStream(socket.inputStream)

    private val outStream = ServerMessageOutputStream(socket.outputStream)

    /**
     * Поток, который слушает сокет от игрока и добавляет принятые сообщения в очередь
     */
    private val inThread = Thread(Runnable {
        try {
            while (true) {
                inputMessageQueue.add(inStream.read())
            }
        }
        catch(ex: InterruptedException) {}
        catch(ex: Exception) {
            println("Deserialization exception")
            disconnect()
        }
    })

    /**
     * Поток, который время от времени отправляет игроку все накопившиеся в очереди сообщения от логики
     */
    private val outThread = Thread(Runnable {
        try {
            while (true) {
                while (outputMessageQueue.isNotEmpty()) {
                    outStream.write(outputMessageQueue.poll())
                }
                outStream.flush()
                Thread.sleep(SERVER_MESSAGE_BUFFER_UPDATE_INTERVAL)
            }
        }
        catch(ex: InterruptedException) {}
    })

    init {
        inThread.start()
        outThread.start()
    }

    /**
     * Разрывает соединение с игроком и вызывает слушателя [disconnectListener]
     */
    fun disconnect() {
        disconnectListener(this@Player)
        inThread.interrupt()
        outThread.interrupt()
        socket.dispose()
    }
}