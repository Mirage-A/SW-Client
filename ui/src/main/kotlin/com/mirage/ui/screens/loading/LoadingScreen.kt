package com.mirage.ui.screens.loading

import com.mirage.core.messaging.ServerMessage
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.ui.screens.AbstractScreen
import com.mirage.ui.screens.ClientMessageListener
import com.mirage.ui.widgets.Widget

class LoadingScreen(virtualScreen: VirtualScreen, listener: ClientMessageListener) : AbstractScreen(virtualScreen) {

    private val loadingState = LoadingState()
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