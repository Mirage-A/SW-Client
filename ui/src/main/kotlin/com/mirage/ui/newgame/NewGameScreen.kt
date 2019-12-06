package com.mirage.ui.newgame

import com.mirage.ui.Screen
import com.mirage.utils.PLATFORM
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import com.mirage.utils.virtualscreen.VirtualScreen
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

    override fun handleServerMessage(msg: ServerMessage) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(virtualScreen: VirtualScreen, currentTimeMillis: Long) {
        uiRenderer.renderUI(virtualScreen, uiState, currentTimeMillis)
    }

    override fun resize(virtualWidth: Float, virtualHeight: Float) = uiState.resize(virtualWidth, virtualHeight)
}