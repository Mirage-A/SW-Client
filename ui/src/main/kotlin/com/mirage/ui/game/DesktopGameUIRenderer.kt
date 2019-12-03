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
        uiState.playerHealthPane.draw(virtualScreen, player?.health ?: 0, player?.maxHealth ?: 0)
        val target = uiState.lastRenderedState.entities[uiState.targetID]
        uiState.targetHealthPane.isVisible = target != null
        uiState.targetNameArea.isVisible = target != null
        if (target != null) {
            uiState.targetHealthPane.draw(virtualScreen, target.health, target.maxHealth)
            uiState.targetNameLabel.text = target.name
            uiState.targetNameArea.draw(virtualScreen)
        }
        uiState.confirmExitMessage.draw(virtualScreen)
    }

}