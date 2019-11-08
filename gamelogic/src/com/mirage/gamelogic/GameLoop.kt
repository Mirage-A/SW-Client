package com.mirage.gamelogic

import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.game.states.GameStateSnapshot
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import rx.Observable

internal interface GameLoop {

    fun start()

    val latestState: Observable<Pair<GameObjects, Long>>

    val serverMessages : Observable<ServerMessage>

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