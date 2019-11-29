package com.mirage.ui.game

import com.mirage.utils.virtualscreen.VirtualScreen

class DesktopGameUIRenderer : GameUIRenderer {

    override fun renderUI(virtualScreen: VirtualScreen, uiState: GameUIState, currentTimeMillis: Long) {
        for (btn in uiState.microMenuBtnList) {
            virtualScreen.draw(btn.getCurrentTextureName(), btn.rect)
            btn.boundedLabel?.draw()
        }
        if (uiState.settingsMenuVisible) {
            for (btn in uiState.settingsMenuBtnList) {
                virtualScreen.draw(btn.getCurrentTextureName(), btn.rect)
                btn.boundedLabel?.draw()
            }
        }
        if (uiState.confirmExitMessage.isVisible) {
            uiState.confirmExitMessage.draw(virtualScreen)
        }
    }

}