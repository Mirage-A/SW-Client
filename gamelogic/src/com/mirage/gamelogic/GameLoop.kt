package com.mirage.gamelogic

import com.mirage.utils.maps.GameStateSnapshot
import com.mirage.utils.messaging.ClientMessage
import io.netty.util.concurrent.Promise
import rx.Observable

internal interface GameLoop {

    fun start()

    val observable : Observable<GameStateSnapshot>

    /**
     * Выполняет запрос на добавление нового игрока и возвращает его ID при добавлении.
     * Метод блокирует поток до добавления игрока.
     */
    fun addNewPlayer() : Long

    fun pause()

    fun resume()

    fun stop()

    fun dispose()

    /**
     * Добавляет сообщение [msg] в очередь сообщений.
     * [id] - ID персонажа игрока (не путать с playerID, используемое в модуле server!)
     */
    fun handleMessage(id: Long, msg: ClientMessage)

}