package com.mirage.ui.loading

import com.mirage.core.datastructures.Point
import com.mirage.core.messaging.ChangeSceneClientMessage
import com.mirage.core.messaging.ClientMessage
import com.mirage.core.messaging.EventSubjectAdapter
import com.mirage.core.messaging.ExitClientMessage
import com.mirage.core.preferences.Prefs
import rx.subjects.Subject
import kotlin.math.min

class DesktopLoadingInputProcessor(private val uiState: LoadingUIState) : LoadingInputProcessor {

    override val inputMessages: Subject<ClientMessage, ClientMessage> = EventSubjectAdapter()

    init {
        uiState.startGameBtn.onPressed = {
            Prefs.profile.currentMap = uiState.gameMapName
            Prefs.savePreferences()
            inputMessages.onNext(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.SINGLEPLAYER_GAME))
        }
        uiState.openInventoryBtn.onPressed = {
            uiState.inventoryWindow.open()
        }
        uiState.openSkillsBtn.onPressed = {
            //TODO Skills widget
        }
        uiState.mainMenuBtn.onPressed = {
            inputMessages.onNext(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.MAIN_MENU))
        }
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.compositeWidget.unpress()
        uiState.compositeWidget.touchUp(virtualPoint)
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        1 + 1 == 2
        uiState.compositeWidget.mouseMoved(virtualPoint)
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
        uiState.compositeWidget.touchDown(virtualPoint)
        return false
    }

    private fun getVirtualPoint(screenX: Int, screenY: Int) =
            uiState.virtualScreen.projectRealPointOnVirtualScreen(Point(screenX.toFloat(), screenY.toFloat()))

}