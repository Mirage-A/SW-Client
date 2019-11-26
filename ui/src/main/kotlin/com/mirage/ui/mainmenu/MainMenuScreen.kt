package com.mirage.ui.mainmenu

import com.mirage.ui.Screen
import com.mirage.utils.PLATFORM
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import com.mirage.utils.virtualscreen.VirtualScreen
import rx.Observable

class MainMenuScreen(virtualScreen: VirtualScreen) : Screen {

    override val inputProcessor: MainMenuInputProcessor = when (PLATFORM) {
        "desktop", "test" -> DesktopMainMenuInputProcessor()
        else -> DesktopMainMenuInputProcessor()
    }

    private val uiRenderer : MainMenuUIRenderer = when (PLATFORM) {
        "desktop", "test" -> DesktopMainMenuUIRenderer(virtualScreen)
        else -> DesktopMainMenuUIRenderer(virtualScreen)
    }

    override val inputMessages: Observable<ClientMessage> = inputProcessor.inputMessages

    override fun handleServerMessage(msg: ServerMessage) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(virtualScreen: VirtualScreen, currentTimeMillis: Long) {
        uiRenderer.renderUI(virtualScreen, inputProcessor.uiState, currentTimeMillis)
    }
}