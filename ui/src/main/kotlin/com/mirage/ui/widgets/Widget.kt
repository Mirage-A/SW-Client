package com.mirage.ui.widgets

import com.mirage.core.utils.Point
import com.mirage.core.utils.Rectangle
import com.mirage.core.virtualscreen.VirtualScreen

internal typealias SizeUpdater = (virtualWidth: Float, virtualHeight: Float) -> Rectangle

internal interface Widget {

    var isVisible: Boolean

    fun draw(virtualScreen: VirtualScreen)

    fun resize(virtualWidth: Float, virtualHeight: Float)

    fun touchUp(virtualPoint: Point, pointer: Int, button: Int): Boolean = touchUp(virtualPoint)

    fun touchUp(virtualPoint: Point): Boolean = false

    fun touchDown(virtualPoint: Point, pointer: Int, button: Int): Boolean = touchDown(virtualPoint)

    fun touchDown(virtualPoint: Point): Boolean = false

    fun mouseMoved(virtualPoint: Point): Boolean = false

    fun keyTyped(character: Char): Boolean = false

    fun scrolled(amount: Int): Boolean = false

    fun touchDragged(virtualPoint: Point, pointer: Int): Boolean = touchDragged(virtualPoint)

    fun touchDragged(virtualPoint: Point): Boolean = false

    fun keyUp(keycode: Int): Boolean = false

    fun keyDown(keycode: Int): Boolean = false

}