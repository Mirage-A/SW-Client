package com.mirage.connection

import com.mirage.utils.game.objects.MoveDirection
import com.mirage.utils.messaging.*
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Реализация интерфейса [Connection], отвечающая за подключение к удаленному серверу.
 */
class RemoteConnection : Connection {

    override val observable: Observable<ServerMessage> = PublishSubject.create()

    override fun close() = TODO("not implemented")

    private var playerID: Long? = null

    /**
     * Отправляет сообщение [msg] на сервер
     */
    fun sendMessage(msg: ClientMessage) : Unit = TODO("not implemented")

    fun sendAndFlush(msg: ClientMessage) : Unit = TODO("not implemented")

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

    override fun getPlayerID(): Long? = playerID

    override var bufferedMoveDirection: MoveDirection? = null

    override var bufferedMoving: Boolean? = null

}