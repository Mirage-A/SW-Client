package com.mirage.ui.loading

import com.mirage.core.DEFAULT_SCREEN_HEIGHT
import com.mirage.core.DEFAULT_SCREEN_WIDTH
import com.mirage.core.virtualscreen.VirtualScreen
import java.lang.Float.max

class DesktopLoadingUIRenderer : LoadingUIRenderer {

    override fun renderUI(virtualScreen: VirtualScreen, uiState: LoadingUIState, currentTimeMillis: Long) {
        uiState.compositeWidget.draw(virtualScreen)
    }
}