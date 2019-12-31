package com.mirage.client

import com.badlogic.gdx.InputProcessor
import com.mirage.ui.screens.Screen

/** Adapter for [Screen] and [InputProcessor] GDX interface */
internal fun Screen.asInputProcessor(): InputProcessor = object : InputProcessor {

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean =
            this@asInputProcessor.touchUp(screenX, screenY, pointer, button)

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean =
            this@asInputProcessor.mouseMoved(screenX, screenY)

    override fun keyTyped(character: Char): Boolean = this@asInputProcessor.keyTyped(character)

    override fun scrolled(amount: Int): Boolean = this@asInputProcessor.scrolled(amount)

    override fun keyUp(keycode: Int): Boolean = this@asInputProcessor.keyUp(keycode)

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean =
            this@asInputProcessor.touchDragged(screenX, screenY, pointer)

    override fun keyDown(keycode: Int): Boolean = this@asInputProcessor.keyDown(keycode)

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean =
            this@asInputProcessor.touchDown(screenX, screenY, pointer, button)
}