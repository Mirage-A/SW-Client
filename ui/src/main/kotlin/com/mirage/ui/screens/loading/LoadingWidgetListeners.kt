package com.mirage.ui.screens.loading

import com.mirage.core.messaging.ChangeSceneClientMessage
import com.mirage.core.preferences.Prefs
import com.mirage.ui.screens.ClientMessageListener

internal fun LoadingWidgets.initializeListeners(loadingState: LoadingState, listener: ClientMessageListener) {
    startGameBtn.onPressed = {
        Prefs.profile.currentMap = loadingState.gameMapName
        Prefs.savePreferences()
        listener(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.SINGLEPLAYER_GAME))
    }
    openInventoryBtn.onPressed = {
        inventoryWindow.open()
    }
    openSkillsBtn.onPressed = {
        //TODO Skills widget
    }
    mainMenuBtn.onPressed = {
        listener(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.MAIN_MENU))
    }
}