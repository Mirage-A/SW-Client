package com.mirage.ui.mainmenu

import com.mirage.utils.datastructures.Point
import com.mirage.utils.messaging.ChangeSceneClientMessage
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.EventSubjectAdapter
import com.mirage.utils.messaging.ExitClientMessage
import rx.subjects.Subject

class DesktopMainMenuInputProcessor(private val uiState: MainMenuUIState) : MainMenuInputProcessor {

    override val inputMessages: Subject<ClientMessage, ClientMessage> = EventSubjectAdapter()

    init {
        uiState.singlePlayerBtn.onPressed = {
            inputMessages.onNext(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.SINGLEPLAYER_GAME))
        }
        uiState.multiPlayerBtn.onPressed = {
            inputMessages.onNext(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.MULTIPLAYER_LOBBY))
        }
        uiState.settingsBtn.onPressed = {
            inputMessages.onNext(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.SETTINGS_MENU))
        }
        uiState.exitBtn.onPressed = {
            inputMessages.onNext(ExitClientMessage(0))
        }
        uiState.changeProfileBtn.onPressed = {
            uiState.profileWindow.isVisible = !uiState.profileWindow.isVisible
        }
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.widgets.forEach { it.touchUp(virtualPoint) }
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
        uiState.widgets.forEach { it.touchDown(virtualPoint) }
        return false
    }

    private fun getVirtualPoint(screenX: Int, screenY: Int) =
            uiState.virtualScreen.projectRealPointOnVirtualScreen(Point(screenX.toFloat(), screenY.toFloat()))

}