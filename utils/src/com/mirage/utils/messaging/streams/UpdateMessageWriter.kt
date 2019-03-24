package com.mirage.utils.messaging.streams

import com.mirage.utils.messaging.UpdateMessage


//TODO ОПТИМИЗАЦИЯ - СЕРИАЛИЗОВАТЬ ДО РАСПРЕДЕЛЕНИЯ, ЧТОБЫ НЕ СЕРИАЛИЗОВЫВАТЬ ОДНО СООБЩЕНИЕ ДЛЯ КАЖДОГО ИГРОКА
/**
 * Интерфейс, позволяющий отправлять сообщения от логики.
 * Используется на стороне логики (сервера).
 */
interface UpdateMessageWriter {

    /**
     * Подготовить сообщение к отправке
     * @see flush
     */
    fun write(msg: UpdateMessage)

    /**
     * Отправить сообщения
     * @see write
     */
    fun flush()

}