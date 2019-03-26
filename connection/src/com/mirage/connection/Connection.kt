package com.mirage.connection

import com.badlogic.gdx.utils.Disposable
import com.mirage.utils.messaging.UpdateMessage
import com.mirage.utils.messaging.MoveDirection

interface Connection : Disposable {

    /**
     * Проверяет, есть ли новые сообщения
     */
    fun hasNewMessages() : Boolean

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
     * Добавляет слушателя новых данных, полученных с сервера.
     * Метод слушателя срабатывает каждый раз, когда с сервера приходят новые данные.
     */
    fun addMessageListener(listener: (msg: UpdateMessage) -> Unit)

    /**
     * Заставляет проверить наличие новых сообщений и вызвать слушателей для каждого сообщения.
     * Этот метод нужно вызывать в методе render клиента //TODO
     */
    fun checkNewMessages()

    /**
     * Возвращает ID игрока
     */
    fun getPlayerID() : Long?

    var bufferedMoveDirection : MoveDirection?

    var bufferedMoving: Boolean?

}