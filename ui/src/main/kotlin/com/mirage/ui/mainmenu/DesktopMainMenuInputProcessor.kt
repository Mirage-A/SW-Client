package com.mirage.ui.mainmenu

import com.mirage.ui.game.GameUIState
import com.mirage.utils.messaging.ChangeSceneClientMessage
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.EventSubjectAdapter
import rx.subjects.Subject

class DesktopMainMenuInputProcessor : MainMenuInputProcessor {

    override val inputMessages: Subject<ClientMessage, ClientMessage> = EventSubjectAdapter()

    override val uiState = MainMenuUIState()

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        inputMessages.onNext(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.SINGLEPLAYER_GAME))
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
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return false
    }
}