package com.mirage.connection

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.mirage.gamelogic.LogicFacade
import com.mirage.utils.*
import com.mirage.utils.Timer
import com.mirage.utils.extensions.get
import com.mirage.utils.extensions.isMoving
import com.mirage.utils.extensions.moveDirection
import com.mirage.utils.messaging.MoveDirection
import com.mirage.utils.messaging.ServerMessage
import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * Реализация интерфейса Connection, работающая с локальным сервером (одиночная игра)
 */
class LocalConnection : Connection {

    override fun readOneMessage() {
        if (messageQueue.isNotEmpty()) {
            queueLock.lock()
            val msg = messageQueue.poll()
            for (msgListener in messageListeners) {
                msgListener(msg)
            }
            queueLock.unlock()
        }
    }

    override fun removeAllMessageListeners() {
        queueLock.lock()
        messageListeners.clear()
        queueLock.unlock()
    }

    override fun hasNewMessages(): Boolean = messageQueue.isNotEmpty()

    override fun dispose() {
        logic.stopLogic()
        logic.dispose()
    }

    private val messageListeners : MutableCollection<(msg: ServerMessage) -> Unit> = ArrayList()

    private val logic : LogicFacade = LogicFacade()

    private var playerID: Long? = null

    override fun getPlayerID(): Long? = playerID

    override var bufferedMoveDirection : MoveDirection? = null
    override var bufferedMoving : Boolean? = null

    private val messageQueue = ArrayDeque<ServerMessage>()
    private val queueLock = ReentrantLock()

    private val messageBufferTimer = Timer(CONNECTION_MESSAGE_BUFFER_UPDATE_INTERVAL) {
        while (!logic.msgs.isEmpty()) {
            queueLock.lock()
            logic.lockMsgQueue()
            val msg = logic.msgs.poll()
            logic.unlockMsgQueue()
            messageQueue.add(msg)
            queueLock.unlock()
        }
    }

    init {
        logic.addUpdateTickListener {
            bufferedMoveDirection?.let {
                logic.objects[playerID]?.moveDirection = it
            }
            bufferedMoving?.let {
                logic.objects[playerID]?.isMoving = it
            }
        }
        messageBufferTimer.start()
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

    override fun addMessageListener(listener: (msg: ServerMessage) -> Unit) {
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

    fun startGame() {
        //TODO выбор карты
        logic.map = logic.loadMap("${Assets.assetsPath}maps/test.tmx", InternalFileHandleResolver())
        logic.startGame()
        playerID = logic.addNewPlayer()
    }

    fun startLogic() = logic.startLogic()

    override fun blockUntilHaveMessages() {
        while(!hasNewMessages()) {
            Thread.sleep(1L)
        }
    }

}