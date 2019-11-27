package com.mirage.ui

import com.badlogic.gdx.InputProcessor
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import com.mirage.utils.virtualscreen.VirtualScreen
import rx.Observable

interface Screen {

    val inputMessages: Observable<ClientMessage>

    val inputProcessor: InputProcessor

    fun handleServerMessage(msg: ServerMessage)

    fun resize(virtualWidth: Float, virtualHeight: Float)

    fun render(virtualScreen: VirtualScreen, currentTimeMillis: Long)

}