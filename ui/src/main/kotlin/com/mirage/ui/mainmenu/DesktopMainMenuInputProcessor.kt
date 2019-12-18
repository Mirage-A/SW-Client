package com.mirage.ui.mainmenu

import com.mirage.utils.datastructures.Point
import com.mirage.utils.messaging.ChangeSceneClientMessage
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.EventSubjectAdapter
import com.mirage.utils.messaging.ExitClientMessage
import com.mirage.utils.preferences.Prefs
import rx.subjects.Subject
import kotlin.math.min

class DesktopMainMenuInputProcessor(private val uiState: MainMenuUIState) : MainMenuInputProcessor {

    override val inputMessages: Subject<ClientMessage, ClientMessage> = EventSubjectAdapter()

    init {
        uiState.singlePlayerBtn.onPressed = {
            inputMessages.onNext(ChangeSceneClientMessage(
                    if (uiState.newGame) ChangeSceneClientMessage.Scene.NEW_PROFILE_MENU
                    else ChangeSceneClientMessage.Scene.SINGLEPLAYER_GAME
            ))
        }
        if (!uiState.newGame) uiState.multiPlayerBtn.onPressed = {
            //TODO
            uiState.inventoryWindow.open()
            inputMessages.onNext(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.MULTIPLAYER_LOBBY))
        }
        uiState.settingsBtn.onPressed = {
            inputMessages.onNext(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.SETTINGS_MENU))
        }
        uiState.exitBtn.onPressed = {
            inputMessages.onNext(ExitClientMessage(0))
        }
        if (!uiState.newGame) {
            uiState.profilePageNavigator.pageCount = Prefs.account.profiles.size / profileBtnCount + 1
            uiState.changeProfileBtn.onPressed = {
                uiState.profileWindow.isVisible = !uiState.profileWindow.isVisible
            }
            uiState.profilePageNavigator.onPageSwitch = {
                loadProfilePage(it)
            }
            loadProfilePage(0)
        }
    }

    private fun loadProfilePage(page: Int) {
        val startIndex = profileBtnCount * page
        val btnCount = min(profileBtnCount, Prefs.account.profiles.size - startIndex)

        if (btnCount < 0) return
        for (i in 0 until btnCount) {
            with (uiState.profileWindowButtons[i]) {
                isVisible = true
                boundedLabel?.text = Prefs.account.profiles[startIndex + i]
                onPressed = {
                    val profileName = Prefs.account.profiles[startIndex + i]
                    uiState.profileWindow.isVisible = false
                    uiState.profileNameArea.boundedLabel?.text = profileName
                    Prefs.switchProfile(profileName)
                }
            }
        }
        if (btnCount < profileBtnCount) {
            with (uiState.profileWindowButtons[btnCount]) {
                isVisible = true
                boundedLabel?.text = "+ New profile +"
                onPressed = {
                    inputMessages.onNext(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.NEW_PROFILE_MENU))
                }
            }
        }
        for (i in btnCount + 1 until profileBtnCount) {
            uiState.profileWindowButtons[i].isVisible = false
        }
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.widgets.forEach { if (it.touchUp(virtualPoint)) return true }
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.widgets.forEach { it.mouseMoved(virtualPoint) }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.widgets.forEach { if (it.touchDown(virtualPoint)) return true }
        return false
    }

    private fun getVirtualPoint(screenX: Int, screenY: Int) =
            uiState.virtualScreen.projectRealPointOnVirtualScreen(Point(screenX.toFloat(), screenY.toFloat()))

}