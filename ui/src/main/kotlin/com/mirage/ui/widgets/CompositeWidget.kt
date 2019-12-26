package com.mirage.ui.widgets

import com.mirage.core.datastructures.Point
import com.mirage.core.virtualscreen.VirtualScreen

/** Composes given widgets into one. Input events are processed in straight order, rendering performs in reversed order */
internal class CompositeWidget(vararg widget: Widget) : Widget {

    private val widgets = widget.toList()

    override var isVisible = true

    override fun resize(virtualWidth: Float, virtualHeight: Float) =
            widgets.forEach { it.resize(virtualWidth, virtualHeight) }

    override fun touchUp(virtualPoint: Point): Boolean =
            isVisible && widgets.any { it.touchUp(virtualPoint) }

    override fun touchDown(virtualPoint: Point): Boolean =
            isVisible && widgets.any { it.touchDown(virtualPoint) }

    override fun mouseMoved(virtualPoint: Point): Boolean {
        var processed = false
        widgets.forEach { if (it.mouseMoved(virtualPoint)) processed = true }
        return processed
    }

    override fun keyTyped(character: Char): Boolean =
            isVisible && widgets.any { it.keyTyped(character) }

    override fun scrolled(amount: Int): Boolean =
            isVisible && widgets.any { it.scrolled(amount) }

    override fun touchDragged(virtualPoint: Point): Boolean =
            isVisible && widgets.any { it.touchDragged(virtualPoint) }

    override fun keyUp(keycode: Int): Boolean =
            isVisible && widgets.any { it.keyUp(keycode) }

    override fun keyDown(keycode: Int): Boolean =
            isVisible && widgets.any { it.keyDown(keycode) }

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) {
            for (i in widgets.size - 1 downTo 0) {
                widgets[i].draw(virtualScreen)
            }
        }
    }
}