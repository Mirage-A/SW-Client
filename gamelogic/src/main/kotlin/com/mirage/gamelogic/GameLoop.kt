package com.mirage.gamelogic

import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import rx.Observable

internal interface GameLoop {

    fun start()

    fun addNewPlayer(onComplete: (playerID: Long) -> Unit)

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