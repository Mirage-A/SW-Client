package com.mirage.ui.screens.newgame

import com.mirage.core.messaging.ServerMessage
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.ui.screens.AbstractScreen
import com.mirage.ui.screens.ClientMessageListener
import com.mirage.ui.widgets.Widget

class NewGameScreen(virtualScreen: VirtualScreen, listener: ClientMessageListener) : AbstractScreen(virtualScreen) {

    private val newGameState = NewGameState()
    private val newGameWidgets = NewGameWidgets(virtualScreen).apply {
        initializeSizeUpdaters()
        initializeListeners(newGameState, listener)
    }

    override val rootWidget: Widget = newGameWidgets.rootWidget

    override fun handleServerMessage(msg: ServerMessage) {}

    init {
        resize(virtualScreen.width, virtualScreen.height)
    }
}