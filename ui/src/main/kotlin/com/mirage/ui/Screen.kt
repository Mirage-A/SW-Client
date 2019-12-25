package com.mirage.ui

import com.badlogic.gdx.InputProcessor
import com.mirage.core.messaging.ClientMessage
import com.mirage.core.messaging.ServerMessage
import com.mirage.core.virtualscreen.VirtualScreen
import rx.Observable

interface Screen {

    val inputMessages: Observable<ClientMessage>

    val inputProcessor: InputProcessor

    fun handleServerMessage(msg: ServerMessage)

    fun resize(virtualWidth: Float, virtualHeight: Float)

    fun render(virtualScreen: VirtualScreen, currentTimeMillis: Long)

}