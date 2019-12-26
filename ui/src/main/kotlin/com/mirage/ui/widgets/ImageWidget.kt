package com.mirage.ui.widgets

import com.mirage.core.datastructures.Point
import com.mirage.core.datastructures.Rectangle
import com.mirage.core.virtualscreen.VirtualScreen

internal class ImageWidget(
        var textureName: String = "null",
        var sizeUpdater: SizeUpdater? = null
) : Widget {

    private var rect: Rectangle = Rectangle()

    override var isVisible = true

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        rect = sizeUpdater?.invoke(virtualWidth, virtualHeight) ?: Rectangle()
    }

    override fun touchUp(virtualPoint: Point): Boolean = isVisible && rect.contains(virtualPoint)

    override fun touchDown(virtualPoint: Point): Boolean = isVisible && rect.contains(virtualPoint)

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) virtualScreen.draw(textureName, rect)
    }
}