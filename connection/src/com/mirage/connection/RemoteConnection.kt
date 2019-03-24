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

class RemoteConnection : Connection {

    private val messageListeners : MutableCollection<(msg: UpdateMessage) -> Unit> = ArrayList()

    private var playerID: Long? = null

    private val messageQueue = ArrayDeque<UpdateMessage>()
    private val queueLock = ReentrantLock()

    private val socket : Socket = NetJavaSocketImpl(Net.Protocol.TCP, SERVER_ADDRESS, SERVER_PORT, SocketHints())

    private val reader : UpdateMessageReader = UpdateMessageInputStream(socket.inputStream)

    private val writer : ClientMessageWriter = ClientMessageOutputStream(socket.outputStream)

    private val readerThread = Thread(Runnable {
        while (true) {
            val msg = reader.read()
            queueLock.lock()
            messageQueue.add(msg)
            queueLock.unlock()
        }
    }).apply {
        start()
    }

    private val writeLock = ReentrantLock()

    fun sendMessage(msg: ClientMessage) {
        writeLock.lock()
        writer.write(msg)
        writeLock.unlock()
    }

    private val bufferWriterTimer = Timer(SERVER_MESSAGE_BUFFER_UPDATE_INTERVAL) {
        bufferedMoveDirection?.let {
            sendMessage(MoveDirectionClientMessage(it))
        }
        bufferedMoving?.let {
            sendMessage(SetMovingClientMessage(it))
        }
    }.apply {
        start()
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