package com.mirage.ui.screens.game

import com.mirage.core.messaging.ChangeSceneClientMessage
import com.mirage.ui.screens.ClientMessageListener

internal fun GameWidgets.initializeListeners(gameState: GameState, listener: ClientMessageListener) {
    settingsBtn.onPressed = {
        settingsMenu.isVisible = !settingsMenu.isVisible
    }
    questsBtn.onPressed = {
        questWindow.isVisible = !questWindow.isVisible
    }
    leaveGameBtn.onPressed = {
        confirmExitMessage.isVisible = true
    }
    confirmExitMessage.setOkAction {
        listener(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.MAIN_MENU))
        confirmExitMessage.isVisible = false
    }
    confirmExitMessage.setCancelAction {
        confirmExitMessage.isVisible = false
    }
    retryBtn.onPressed = {
        listener(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.LOADING_SCREEN))
    }
    mainMenuBtn.onPressed = {
        listener(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.MAIN_MENU))
    }
    questWindow.updateQuestWindow()
    updateSkillBtns(gameState)
}


/** Updates skill buttons images and cooldown text */
private fun GameWidgets.updateSkillBtns(gameState: GameState) {
    for (i in 0 until 5) {
        val skill = gameState.skillNames[i]
        val btn = if (i == 4) ultimateSkillBtn else activeSkills[i]
        val cd = gameState.skillCoolDowns[i]
        if (skill == null) {
            btn.isVisible = false
        }
        else {
            btn.isVisible = true
            btn.textureName = if (cd == 0L) "skills/colored/$skill" else "skills/uncolored/$skill"
            btn.highlightedTextureName = btn.textureName
            btn.boundedLabel?.text = if (cd == 0L) "" else cd.toString()
        }
    }
}