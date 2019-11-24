package com.mirage.ui

import com.badlogic.gdx.InputProcessor
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import com.mirage.utils.virtualscreen.VirtualScreen
import rx.Observable

interface Screen : InputProcessor {

    val inputMessages: Observable<ClientMessage>

    fun handleServerMessage(msg: ServerMessage)

    fun render(virtualScreen: VirtualScreen, currentTimeMillis: Long)

}