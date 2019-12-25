package com.mirage.ui.widgets

import com.mirage.core.datastructures.Point
import com.mirage.core.datastructures.Rectangle
import com.mirage.core.virtualscreen.VirtualScreen

class LabelWidget(val label: VirtualScreen.Label, var sizeUpdater: (Float, Float) -> Rectangle) : Widget {

    var text: String
        get() = label.text
        set(value) { label.text = value }

    override var isVisible = true

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        label.rect = sizeUpdater(virtualWidth, virtualHeight)
        label.resizeFont(virtualWidth, virtualHeight)
    }

    override fun touchUp(virtualPoint: Point): Boolean = false

    override fun touchDown(virtualPoint: Point): Boolean = false

    override fun mouseMoved(virtualPoint: Point) {}

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) label.draw()
    }
}