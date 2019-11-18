package com.mirage.utils.messaging.streams

import com.mirage.utils.messaging.ServerMessage


//TODO ОПТИМИЗАЦИЯ - СЕРИАЛИЗОВАТЬ ДО РАСПРЕДЕЛЕНИЯ, ЧТОБЫ НЕ СЕРИАЛИЗОВЫВАТЬ ОДНО СООБЩЕНИЕ ДЛЯ КАЖДОГО ИГРОКА
/**
 * Интерфейс, позволяющий отправлять сообщения от логики.
 * Используется на стороне логики (сервера).
 */
interface ServerMessageWriter {

    /**
     * Подготовить сообщение к отправке
     * @see flush
     */
    fun write(msg: ServerMessage)

    /**
     * Отправить сообщения
     * @see write
     */
    fun flush()

}