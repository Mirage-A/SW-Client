package com.mirage.ui.newgame

import com.mirage.ui.Screen
import com.mirage.core.PLATFORM
import com.mirage.core.messaging.ClientMessage
import com.mirage.core.messaging.ServerMessage
import com.mirage.core.virtualscreen.VirtualScreen
import rx.Observable

class NewGameScreen(virtualScreen: VirtualScreen) : Screen {

    private val uiState: NewGameUIState = NewGameUIState(virtualScreen)

    override val inputProcessor: NewGameInputProcessor = when (PLATFORM) {
        "desktop", "test" -> DesktopNewGameInputProcessor(uiState)
        else -> DesktopNewGameInputProcessor(uiState)
    }

    private val uiRenderer : NewGameUIRenderer = when (PLATFORM) {
        "desktop", "test" -> DesktopNewGameUIRenderer()
        else -> DesktopNewGameUIRenderer()
    }

    override val inputMessages: Observable<ClientMessage> = inputProcessor.inputMessages

    override fun handleServerMessage(msg: ServerMessage) {}

    override fun render(virtualScreen: VirtualScreen, currentTimeMillis: Long) {
        uiRenderer.renderUI(virtualScreen, uiState, currentTimeMillis)
    }

    override fun resize(virtualWidth: Float, virtualHeight: Float) = uiState.resize(virtualWidth, virtualHeight)
}