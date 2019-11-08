package com.mirage.connection

import com.mirage.utils.messaging.ServerMessage
import com.mirage.utils.game.objects.MoveDirection
import rx.Observable

interface Connection {

    /**
     * [Observable], который позволяет следить за получаемыми от логики сообщениями.
     */
    val observable: Observable<ServerMessage>

    /**
     * Отправляет запрос на изменение направления движения игрока
     */
    fun setMoveDirection(md: MoveDirection)

    /**
     * Отправляет запрос на движение/остановку игрока
     */
    fun setMoving(isMoving: Boolean)

    /**
     * Отправляет запрос на начало движения в данном направлении
     */
    fun startMoving(md: MoveDirection)

    /**
     * Отправляет запрос на остановку игрока
     */
    fun stopMoving()
    /**
     * Возвращает ID игрока
     */
    fun getPlayerID() : Long?

    var bufferedMoveDirection : MoveDirection?

    var bufferedMoving: Boolean?

    /**
     * Полностью останавливает взаимодействие с логикой и освобождает все ресурсы
     */
    fun close()


}