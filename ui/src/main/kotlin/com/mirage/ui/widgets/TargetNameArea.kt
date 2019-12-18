package com.mirage.ui.widgets

import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.virtualscreen.VirtualScreen

class TargetNameArea(
        var borderTextureName: String,
        var textAreaTextureName: String,
        var rect: Rectangle = Rectangle(),
        var boundedLabel: VirtualScreen.Label? = null,
        var innerMargin: Float,
        var sizeUpdater: ((Float, Float) -> Rectangle)? = null
) : Widget {

    override var isVisible = true

    private val resourceRect: Rectangle
        get() = Rectangle(rect.x, rect.y, rect.width - innerMargin * 2f, rect.height - innerMargin * 2f)

    init {
        boundedLabel?.rect = resourceRect
    }

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        sizeUpdater?.invoke(virtualWidth, virtualHeight)?.let {
            rect = it
            boundedLabel?.rect = resourceRect
        }
    }

    override fun mouseMoved(virtualPoint: Point) {}

    override fun draw(virtualScreen: VirtualScreen) {
        if (!isVisible) return
        virtualScreen.draw(borderTextureName, rect)
        virtualScreen.draw(textAreaTextureName, resourceRect)
        boundedLabel?.draw()

    }

    override fun touchDown(virtualPoint: Point): Boolean = rect.contains(virtualPoint)

    override fun touchUp(virtualPoint: Point): Boolean = rect.contains(virtualPoint)

}