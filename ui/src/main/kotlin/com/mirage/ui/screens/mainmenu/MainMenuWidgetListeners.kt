package com.mirage.ui.screens.mainmenu

import com.mirage.core.messaging.ChangeSceneClientMessage
import com.mirage.core.messaging.ExitClientMessage
import com.mirage.core.preferences.GdxPreferences
import com.mirage.ui.screens.ClientMessageListener
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
    profilePageNavigator.pageCount = GdxPreferences.account.profiles.size / profileBtnCount + 1
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
    val btnCount = min(profileBtnCount, GdxPreferences.account.profiles.size - startIndex)
    if (btnCount < 0) return
    for (i in 0 until btnCount) {
        with(profileWindowButtons[i]) {
            isVisible = true
            boundedLabel?.text = GdxPreferences.account.profiles[startIndex + i]
            onPressed = {
                val profileName = GdxPreferences.account.profiles[startIndex + i]
                profileWindow.isVisible = false
                profileNameArea.boundedLabel?.text = profileName
                GdxPreferences.switchProfile(profileName)
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