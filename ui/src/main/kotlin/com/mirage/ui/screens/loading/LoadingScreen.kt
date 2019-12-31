package com.mirage.ui.screens.loading

import com.mirage.core.messaging.ServerMessage
import com.mirage.core.VirtualScreen
import com.mirage.core.preferences.Preferences
import com.mirage.core.utils.Assets
import com.mirage.ui.screens.Screen
import com.mirage.ui.screens.ClientMessageListener
import com.mirage.ui.widgets.Widget

class LoadingScreen(
        virtualScreen: VirtualScreen,
        assets: Assets,
        preferences: Preferences,
        listener: ClientMessageListener
) : Screen(virtualScreen, listener) {

    private val loadingState = LoadingState(assets, preferences)
    private val loadingWidgets = LoadingWidgets(virtualScreen, loadingState).apply {
        initializeSizeUpdaters()
        initializeListeners(loadingState, listener)
    }

    override val rootWidget: Widget = loadingWidgets.rootWidget

    override fun handleServerMessage(msg: ServerMessage) {}

    init {
        resize(virtualScreen.width, virtualScreen.height)
    }

}