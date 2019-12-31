package com.mirage.ui.screens.newgame

import com.mirage.core.messaging.ServerMessage
import com.mirage.core.VirtualScreen
import com.mirage.core.preferences.Preferences
import com.mirage.core.utils.Assets
import com.mirage.ui.screens.Screen
import com.mirage.ui.screens.ClientMessageListener
import com.mirage.ui.widgets.Widget

class NewGameScreen(
        virtualScreen: VirtualScreen,
        assets: Assets,
        preferences: Preferences,
        listener: ClientMessageListener
) : Screen(virtualScreen, listener) {

    private val newGameState = NewGameState(assets, preferences)
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