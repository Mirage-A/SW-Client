package com.mirage.connection

import com.badlogic.gdx.Net
import com.badlogic.gdx.net.NetJavaSocketImpl
import com.badlogic.gdx.net.Socket
import com.badlogic.gdx.net.SocketHints
import com.mirage.utils.*
import com.mirage.utils.Timer
import com.mirage.utils.messaging.*
import com.mirage.utils.messaging.streams.ClientMessageWriter
import com.mirage.utils.messaging.streams.UpdateMessageReader
import com.mirage.utils.messaging.streams.impls.ClientMessageOutputStream
import com.mirage.utils.messaging.streams.impls.UpdateMessageInputStream
import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * Реализация интерфейса [Connection], отвечающая за подключение к удаленному серверу.
 */
class RemoteConnection : Connection {

    override fun hasNewMessages(): Boolean = messageQueue.isNotEmpty()

    override fun dispose() {
        //TODO
        readerThread.interrupt()
        bufferWriterTimer.stop()
        socket.dispose()
    }

    private val messageListeners : MutableCollection<(msg: UpdateMessage) -> Unit> = ArrayList()

    private var playerID: Long? = null

    private val messageQueue = ArrayDeque<UpdateMessage>()
    private val queueLock = ReentrantLock()

    private val socket : Socket = NetJavaSocketImpl(Net.Protocol.TCP, SERVER_ADDRESS, SERVER_PORT, SocketHints())

    private val reader : UpdateMessageReader = UpdateMessageInputStream(socket.inputStream)

    private val writer : ClientMessageWriter = ClientMessageOutputStream(socket.outputStream)

    /**
     * Поток, который считывает сообщения [UpdateMessage] от сервера и добавляет их в очередь [messageQueue]
     * @see queueLock
     */
    private val readerThread = Thread(Runnable {
        try {
            while (true) {
                val msg = reader.read()
                queueLock.lock()
                messageQueue.add(msg)
                queueLock.unlock()
            }
        }
        catch(ex: InterruptedException) { }
    })

    private val writeLock = ReentrantLock()

    /**
     * Отправляет сообщение [msg] на сервер
     */
    fun sendMessage(msg: ClientMessage) {
        writeLock.lock()
        writer.write(msg)
        writeLock.unlock()
    }

    /**
     * Таймер, который каждые [SERVER_MESSAGE_BUFFER_UPDATE_INTERVAL] мс
     * отправляет на сервер информацию о движении персонажа игрока
     * @see setMoving
     * @see setMoveDirection
     * @see bufferedMoving
     * @see bufferedMoveDirection
     */
    private val bufferWriterTimer = Timer(SERVER_MESSAGE_BUFFER_UPDATE_INTERVAL) {
        bufferedMoveDirection?.let {
            sendMessage(MoveDirectionClientMessage(it))
        }
        bufferedMoving?.let {
            sendMessage(SetMovingClientMessage(it))
        }
    }

    init {
        readerThread.start()
        bufferWriterTimer.start()
    }

    override fun setMoveDirection(md: MoveDirection) {
        bufferedMoveDirection = md
    }

    override fun setMoving(isMoving: Boolean) {
        bufferedMoving = isMoving
    }

    override fun startMoving(md: MoveDirection) {
        setMoveDirection(md)
        setMoving(true)
    }

    override fun stopMoving() {
        setMoving(false)
    }

    override fun addMessageListener(listener: (msg: UpdateMessage) -> Unit) {
        messageListeners.add(listener)
    }

    override fun checkNewMessages() {
        if (messageQueue.isNotEmpty()) {
            queueLock.lock()
            while (messageQueue.isNotEmpty()) {
                val msg = messageQueue.poll()
                for (msgListener in messageListeners) {
                    msgListener(msg)
                }
            }
            queueLock.unlock()
        }
    }

    override fun getPlayerID(): Long? = playerID

    override var bufferedMoveDirection: MoveDirection? = null

    override var bufferedMoving: Boolean? = null

}