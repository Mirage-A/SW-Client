package com.mirage.ui.game

import com.mirage.core.virtualscreen.VirtualScreen

internal class DesktopGameUIRenderer : GameUIRenderer {

    override fun renderUI(virtualScreen: VirtualScreen, uiState: GameUIState, currentTimeMillis: Long) {
        if (!uiState.gameOver) {
            val player = uiState.lastRenderedState.entities[uiState.playerID]
            uiState.playerHealthPane.currentResource = player?.health ?: 0
            uiState.playerHealthPane.maxResource = player?.maxHealth ?: 0
            val target = uiState.lastRenderedState.entities[uiState.targetID]
            uiState.targetHealthPane.isVisible = target != null
            uiState.targetNameArea.isVisible = target != null
            if (target != null) {
                uiState.targetHealthPane.currentResource = target.health
                uiState.targetHealthPane.maxResource = target.maxHealth
                uiState.targetNameLabel.text = target.name
            }
        }
        for (i in uiState.widgets.size - 1 downTo 0) {
            uiState.widgets[i].draw(virtualScreen)
        }
    }

}