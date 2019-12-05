package com.mirage.ui.widgets

import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.virtualscreen.VirtualScreen

class LabelWidget(val label: VirtualScreen.Label, var sizeUpdater: (Float, Float) -> Rectangle) : Widget {

    var isVisible = true

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        label.rect = sizeUpdater(virtualWidth, virtualHeight)
    }

    override fun touchUp(virtualPoint: Point): Boolean = false

    override fun touchDown(virtualPoint: Point): Boolean = false

    override fun mouseMoved(virtualPoint: Point) {}

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) label.draw()
    }
}