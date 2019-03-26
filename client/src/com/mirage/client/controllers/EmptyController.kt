package com.mirage.client.controllers

import com.badlogic.gdx.Screen
import com.badlogic.gdx.ScreenAdapter

class EmptyController : Controller {

    override val screen: Screen = ScreenAdapter()

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false

    override fun keyTyped(character: Char): Boolean = false

    override fun scrolled(amount: Int): Boolean = false

    override fun keyUp(keycode: Int): Boolean = false

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false

    override fun keyDown(keycode: Int): Boolean = false

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false

    override fun dispose() { }
}