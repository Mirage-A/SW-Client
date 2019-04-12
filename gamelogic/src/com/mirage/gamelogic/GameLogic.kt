package com.mirage.gamelogic

import com.mirage.utils.maps.GameStateSnapshot
import com.mirage.utils.messaging.ClientMessage
import rx.Observable

/**
 * Фасад логики
 */
interface GameLogic {

    /**
     * Запускает цикл логики
     */
    fun startLogic()

    /**
     * Приостанавливает цикл логики
     */
    fun pauseLogic()

    /**
     * Возобновляет цикл логики после паузы
     */
    fun resumeLogic()

    /**
     * Полностью останавливает логику без возможности перезапуска
     */
    fun stopLogic()

    /**
     * Останавливает логику и освобождает все ресурсы
     */
    fun dispose()

    /**
     * Передаёт сообщение клиента в логику.
     * Сообщение будет обработано при следующем тике цикла логики.
     */
    fun handleMessage(msg: ClientMessage)

    /**
     * Возвращает Observable, на который можно подписаться, чтобы получать уведомления об изменении
     * состояния игры после каждого тика цикла логики.
     */
    val observable : Observable<GameStateSnapshot>
}