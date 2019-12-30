package com.mirage.ui.screens

import com.badlogic.gdx.InputProcessor
import com.mirage.core.messaging.ServerMessage
import com.mirage.core.VirtualScreen

interface Screen : InputProcessor {

    fun handleServerMessage(msg: ServerMessage)

    fun resize(virtualWidth: Float, virtualHeight: Float)

    fun render(virtualScreen: VirtualScreen)

}