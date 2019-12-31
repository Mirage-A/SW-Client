package com.mirage.ui.screens.mainmenu

import com.mirage.core.messaging.ServerMessage
import com.mirage.core.VirtualScreen
import com.mirage.core.preferences.Preferences
import com.mirage.core.utils.Assets
import com.mirage.ui.screens.Screen
import com.mirage.ui.screens.ClientMessageListener
import com.mirage.ui.widgets.Widget

class MainMenuScreen(
        virtualScreen: VirtualScreen,
        assets: Assets,
        preferences: Preferences,
        listener: ClientMessageListener
) : Screen(virtualScreen, listener) {

    private val mainMenuState = MainMenuState(assets, preferences)
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