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
     * Добавляет игрока на карту и возвращает его ID на карте
     * //TODO Передавать информацию о скиллах, экипировке и т.д. игрока
     */
    fun addNewPlayer() : Long

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
     * Добавляет сообщение [msg] в очередь сообщений.
     * [id] - ID персонажа игрока (не путать с playerID, используемое в модуле server!)
     */
    fun handleMessage(id: Long, msg: ClientMessage)

    /**
     * Возвращает Observable, на который можно подписаться, чтобы получать уведомления об изменении
     * состояния игры после каждого тика цикла логики.
     */
    val observable : Observable<GameStateSnapshot>

}