package com.mirage.connection

import com.mirage.gamelogic.LogicFacade
import com.mirage.utils.UpdateMessage
import com.mirage.utils.Log
import com.mirage.utils.MoveDirection
import com.mirage.utils.extensions.get
import com.mirage.utils.extensions.isMoving
import com.mirage.utils.extensions.moveDirection

/**
 * Реализация интерфейса Connection, работающая с локальным сервером (одиночная игра)
 */
class LocalConnection : Connection {

    private val messageListeners : MutableCollection<(msg: UpdateMessage) -> Unit> = ArrayList()

    val logic : LogicFacade = LogicFacade()

    private var playerID: Long? = null

    override fun getPlayerID(): Long? = playerID

    private var bufferedMoveDirection : MoveDirection? = null
    private var bufferedMoving : Boolean? = null

    init {
        logic.addUpdateTickListener {
            bufferedMoveDirection?.let {
                logic.gameLoop.objects[playerID]?.moveDirection = it
                bufferedMoveDirection = null
            }
            bufferedMoving?.let {
                logic.gameLoop.objects[playerID]?.isMoving = it
                bufferedMoving = null
            }
        }
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
        while (!logic.gameLoop.messageQueue.isEmpty()) {
            val msg = logic.gameLoop.messageQueue.pop()
            for (msgListener in messageListeners) {
                msgListener(msg)
            }
        }
    }

    fun startGame() {
        logic.startGame()
        playerID = logic.addNewPlayer()
    }

    fun startLogic() = logic.startLogic()

}