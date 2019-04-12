package com.mirage.gamelogic

import com.mirage.utils.maps.GameStateSnapshot
import com.mirage.utils.messaging.ClientMessage
import rx.Observable

internal interface GameLoop {

    fun start()

    val observable : Observable<GameStateSnapshot>

    fun pause()

    fun resume()

    fun stop()

    fun dispose()

    fun handleMessage(msg: ClientMessage)

}