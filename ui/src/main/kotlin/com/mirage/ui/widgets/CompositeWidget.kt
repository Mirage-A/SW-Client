package com.mirage.ui.widgets

import com.mirage.core.datastructures.Point
import com.mirage.core.virtualscreen.VirtualScreen

class CompositeWidget(vararg widget: Widget) : Widget {

    private val widgets = widget.toList()

    override var isVisible = true

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        widgets.forEach { it.resize(virtualWidth, virtualHeight) }
    }

    override fun touchUp(virtualPoint: Point): Boolean {
        if (isVisible) {
            widgets.forEach {
                if (it.touchUp(virtualPoint)) return true
            }
        }
        return false
    }

    override fun touchDown(virtualPoint: Point): Boolean {
        if (isVisible) {
            widgets.forEach {
                if (it.touchDown(virtualPoint)) return true
            }
        }
        return false
    }

    override fun mouseMoved(virtualPoint: Point) {
        widgets.forEach { it.mouseMoved(virtualPoint) }
    }

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) {
            for (i in widgets.size - 1 downTo 0) {
                widgets[i].draw(virtualScreen)
            }
        }
    }

    override fun unpress() {
        widgets.forEach { it.unpress() }
    }
}