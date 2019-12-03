package com.mirage.gamelogic

import com.mirage.utils.messaging.ClientMessage

/**
 * Фасад логики
 */
interface GameLogic {

    /**
     * Запускает цикл логики
     */
    fun startLogic()

    /**
     * Добавляет игрока на карту и вызывает [onComplete] от его ID.
     * //TODO Передавать информацию о скиллах, экипировке и т.д. игрока
     */
    fun addNewPlayer(onComplete: (playerID: Long) -> Unit)

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

}