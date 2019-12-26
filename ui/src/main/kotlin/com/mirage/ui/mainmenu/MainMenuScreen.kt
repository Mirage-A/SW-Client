package com.mirage.ui.mainmenu

import com.mirage.ui.Screen
import com.mirage.core.PLATFORM
import com.mirage.core.messaging.ClientMessage
import com.mirage.core.messaging.ServerMessage
import com.mirage.core.preferences.Prefs
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.ui.AbstractScreen
import com.mirage.ui.ClientMessageListener
import com.mirage.ui.widgets.Widget
import rx.Observable
import java.util.*

class MainMenuScreen(virtualScreen: VirtualScreen, listener: ClientMessageListener) : AbstractScreen(virtualScreen) {

    private val mainMenuState = MainMenuState()
    private val mainMenuWidgets = MainMenuWidgets(virtualScreen).apply {
        initializeSizeUpdaters(mainMenuState)
        initializeListeners(mainMenuState, listener)
    }

    override val rootWidget: Widget = mainMenuWidgets.rootWidget

    override fun handleServerMessage(msg: ServerMessage) {}

    init {
        resize(virtualScreen.width, virtualScreen.height)
    }

}