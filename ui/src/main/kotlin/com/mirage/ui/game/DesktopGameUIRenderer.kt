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
        val player = uiState.lastRenderedState.entities[uiState.playerID]
        uiState.playerHealthPane.currentResource = player?.health ?: 0
        uiState.playerHealthPane.maxResource = player?.maxHealth ?: 0
        uiState.playerHealthPane.draw(virtualScreen)
        val target = uiState.lastRenderedState.entities[uiState.targetID]
        uiState.targetHealthPane.isVisible = target != null
        uiState.targetNameArea.isVisible = target != null
        if (target != null) {
            uiState.targetHealthPane.currentResource = target.health
            uiState.targetHealthPane.maxResource = target.maxHealth
            uiState.targetHealthPane.draw(virtualScreen)
            uiState.targetNameLabel.text = target.name
            uiState.targetNameArea.draw(virtualScreen)
        }
        uiState.confirmExitMessage.draw(virtualScreen)
    }

}