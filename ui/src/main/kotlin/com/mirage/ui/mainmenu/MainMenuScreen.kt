package com.mirage.ui.mainmenu

import com.mirage.ui.Screen
import com.mirage.utils.PLATFORM
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import com.mirage.utils.preferences.Prefs
import com.mirage.utils.virtualscreen.VirtualScreen
import rx.Observable

class MainMenuScreen(virtualScreen: VirtualScreen) : Screen {

    private val uiState: MainMenuUIState = MainMenuUIState(virtualScreen, Prefs.account.currentProfile == null)

    override val inputProcessor = when (PLATFORM) {
        "desktop", "test" -> DesktopMainMenuInputProcessor(uiState)
        else -> DesktopMainMenuInputProcessor(uiState)
    }

    private val uiRenderer : MainMenuUIRenderer = when (PLATFORM) {
        "desktop", "test" -> DesktopMainMenuUIRenderer()
        else -> DesktopMainMenuUIRenderer()
    }

    override val inputMessages: Observable<ClientMessage> = inputProcessor.inputMessages

    override fun handleServerMessage(msg: ServerMessage) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(virtualScreen: VirtualScreen, currentTimeMillis: Long) {
        uiRenderer.renderUI(virtualScreen, uiState, currentTimeMillis)
    }

    override fun resize(virtualWidth: Float, virtualHeight: Float) = uiState.resize(virtualWidth, virtualHeight)
}