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
        uiState.playerHealthPane.draw(virtualScreen, ((System.currentTimeMillis() / 10L) % 1000L).toInt(), 1000)
        uiState.confirmExitMessage.draw(virtualScreen)
    }

}