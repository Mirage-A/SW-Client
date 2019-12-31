package com.mirage.ui.widgets

import com.mirage.core.utils.Point
import com.mirage.core.utils.Rectangle
import com.mirage.core.VirtualScreen

internal class ImageWidget(
        var textureName: String = "null",
        var sizeUpdater: SizeUpdater? = null,
        override var isVisible: Boolean = true
) : Widget {

    private var rect: Rectangle = Rectangle()

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        rect = sizeUpdater?.invoke(virtualWidth, virtualHeight) ?: Rectangle()
    }

    override fun touchUp(virtualPoint: Point): Boolean = isVisible && rect.contains(virtualPoint)

    override fun touchDown(virtualPoint: Point): Boolean = isVisible && rect.contains(virtualPoint)

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) virtualScreen.draw(textureName, rect)
    }
}