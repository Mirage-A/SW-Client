package com.mirage.ui.loading

import com.mirage.ui.Screen
import com.mirage.core.PLATFORM
import com.mirage.core.extensions.GameMapName
import com.mirage.core.messaging.ClientMessage
import com.mirage.core.messaging.ServerMessage
import com.mirage.core.preferences.Prefs
import com.mirage.core.virtualscreen.VirtualScreen
import rx.Observable

class LoadingScreen(virtualScreen: VirtualScreen, gameMapName: GameMapName) : Screen {

    private val uiState: LoadingUIState = LoadingUIState(virtualScreen, gameMapName)

    override val inputProcessor = when (PLATFORM) {
        "desktop", "test" -> DesktopLoadingInputProcessor(uiState)
        else -> DesktopLoadingInputProcessor(uiState)
    }

    private val uiRenderer : LoadingUIRenderer = when (PLATFORM) {
        "desktop", "test" -> DesktopLoadingUIRenderer()
        else -> DesktopLoadingUIRenderer()
    }

    override val inputMessages: Observable<ClientMessage> = inputProcessor.inputMessages

    override fun handleServerMessage(msg: ServerMessage) {
        //TODO Maybe implement waiting other players in multi-player, or move it to multiplayer lobby
    }

    override fun render(virtualScreen: VirtualScreen, currentTimeMillis: Long) {
        uiRenderer.renderUI(virtualScreen, uiState, currentTimeMillis)
    }

    override fun resize(virtualWidth: Float, virtualHeight: Float) = uiState.resize(virtualWidth, virtualHeight)
}