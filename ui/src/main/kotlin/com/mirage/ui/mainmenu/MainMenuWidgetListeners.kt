package com.mirage.ui.mainmenu

import com.mirage.core.messaging.ChangeSceneClientMessage
import com.mirage.core.messaging.ExitClientMessage
import com.mirage.core.preferences.Prefs
import com.mirage.ui.ClientMessageListener
import kotlin.math.min

internal fun MainMenuWidgets.initializeListeners(mainMenuState: MainMenuState, listener: ClientMessageListener) {
    singlePlayerBtn.onPressed = {
        listener(ChangeSceneClientMessage(
                if (mainMenuState.newGame) ChangeSceneClientMessage.Scene.NEW_PROFILE_MENU
                else ChangeSceneClientMessage.Scene.SINGLEPLAYER_GAME
        ))
    }
    multiPlayerBtn.onPressed = {
        listener(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.MULTIPLAYER_LOBBY))
    }
    settingsBtn.onPressed = {
        listener(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.SETTINGS_MENU))
    }
    exitBtn.onPressed = {
        listener(ExitClientMessage(0))
    }
    profilePageNavigator.pageCount = Prefs.account.profiles.size / profileBtnCount + 1
    changeProfileBtn.onPressed = {
        profileWindow.isVisible = !profileWindow.isVisible
    }
    profilePageNavigator.onPageSwitch = {
        loadProfilePage(it, listener)
    }
    loadProfilePage(0, listener)
}

private fun MainMenuWidgets.loadProfilePage(page: Int, listener: ClientMessageListener) {
    val startIndex = profileBtnCount * page
    val btnCount = min(profileBtnCount, Prefs.account.profiles.size - startIndex)
    if (btnCount < 0) return
    for (i in 0 until btnCount) {
        with(profileWindowButtons[i]) {
            isVisible = true
            boundedLabel?.text = Prefs.account.profiles[startIndex + i]
            onPressed = {
                val profileName = Prefs.account.profiles[startIndex + i]
                profileWindow.isVisible = false
                profileNameArea.boundedLabel?.text = profileName
                Prefs.switchProfile(profileName)
            }
        }
    }
    if (btnCount < profileBtnCount) {
        with(profileWindowButtons[btnCount]) {
            isVisible = true
            boundedLabel?.text = "+ New profile +"
            onPressed = {
                listener(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.NEW_PROFILE_MENU))
            }
        }
    }
    for (i in btnCount + 1 until profileBtnCount) {
        profileWindowButtons[i].isVisible = false
    }
}