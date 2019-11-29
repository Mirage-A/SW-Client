package com.mirage.ui.game

import com.mirage.utils.virtualscreen.VirtualScreen

class DesktopGameUIRenderer : GameUIRenderer {

    override fun renderUI(virtualScreen: VirtualScreen, uiState: GameUIState, currentTimeMillis: Long) {
        for (btn in uiState.microMenuBtnList) {
            btn.draw(virtualScreen)
        }
        for (btn in uiState.settingsMenuBtnList) {
            btn.draw(virtualScreen)
        }
        for (btn in uiState.skillBtns) {
            btn.draw(virtualScreen)
        }
        uiState.confirmExitMessage.draw(virtualScreen)
    }

}