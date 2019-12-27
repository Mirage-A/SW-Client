package com.mirage.ui.screens.mainmenu

import com.mirage.core.messaging.ServerMessage
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.ui.screens.AbstractScreen
import com.mirage.ui.screens.ClientMessageListener
import com.mirage.ui.widgets.Widget

class MainMenuScreen(virtualScreen: VirtualScreen, listener: ClientMessageListener) : AbstractScreen(virtualScreen) {

    private val mainMenuState = MainMenuState()
    private val mainMenuWidgets = MainMenuWidgets(virtualScreen, mainMenuState).apply {
        initializeSizeUpdaters(mainMenuState)
        initializeListeners(mainMenuState, listener)
    }

    override val rootWidget: Widget = mainMenuWidgets.rootWidget

    override fun handleServerMessage(msg: ServerMessage) {}

    init {
        resize(virtualScreen.width, virtualScreen.height)
    }

}