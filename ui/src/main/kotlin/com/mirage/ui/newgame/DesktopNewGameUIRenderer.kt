package com.mirage.ui.newgame

import com.mirage.core.virtualscreen.VirtualScreen

class DesktopNewGameUIRenderer : NewGameUIRenderer {

    override fun renderUI(virtualScreen: VirtualScreen, uiState: NewGameUIState, currentTimeMillis: Long) {
        for (i in uiState.widgets.size - 1 downTo 0) {
            uiState.widgets[i].draw(virtualScreen)
        }
        uiState.textField?.draw()
    }

}