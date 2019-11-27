package com.mirage.ui.mainmenu

import com.mirage.ui.game.GameUIState
import com.mirage.utils.datastructures.Point
import com.mirage.utils.messaging.ChangeSceneClientMessage
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.EventSubjectAdapter
import com.mirage.utils.messaging.ExitClientMessage
import rx.subjects.Subject
import kotlin.system.exitProcess

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
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        for (btn in uiState.btnList) {
            if (btn.rect.contains(virtualPoint)) {
                btn.onPressed()
            }
            btn.isPressed = false
        }
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        for (btn in uiState.btnList) {
            btn.isHighlighted = btn.rect.contains(virtualPoint)
        }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        println("keyDown $keycode")
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        for (btn in uiState.btnList) {
            btn.isPressed = btn.rect.contains(virtualPoint)
        }
        return false
    }

    private fun getVirtualPoint(screenX: Int, screenY: Int) =
            uiState.virtualScreen.projectRealPointOnVirtualScreen(Point(screenX.toFloat(), screenY.toFloat()))

}