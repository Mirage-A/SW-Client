package com.mirage.ui.game

import com.badlogic.gdx.InputProcessor
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.EventSubjectAdapter
import rx.Observable

class DesktopGameInputProcessor : GameInputProcessor {

    override val inputMessages: Observable<ClientMessage> = EventSubjectAdapter()

    override val uiState = GameUIState()

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        println("touchUp $screenX $screenY $pointer $button")
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        println("mouseMoved $screenX $screenY")
        return true
    }

    override fun keyTyped(character: Char): Boolean {
        println("keyTyped $character")
        return true
    }

    override fun scrolled(amount: Int): Boolean {
        println("scrolled $amount")
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        println("keyUp $keycode")
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        println("touchDragged $screenX $screenY $pointer")
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        println("keyDown $keycode")
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        println("touchDown $screenX $screenY $pointer $button")
        return true
    }

}