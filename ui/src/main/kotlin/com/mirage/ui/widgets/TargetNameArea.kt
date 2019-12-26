package com.mirage.ui.widgets

import com.mirage.core.datastructures.Point
import com.mirage.core.datastructures.Rectangle
import com.mirage.core.virtualscreen.VirtualScreen

internal class TargetNameArea(
        var borderTextureName: String = "ui/game/health-border",
        var textAreaTextureName: String = "ui/game/health-lost",
        var boundedLabel: VirtualScreen.Label? = null,
        var innerMargin: Float = 0f,
        var sizeUpdater: SizeUpdater? = null
) : Widget {

    override var isVisible = true

    private var rect: Rectangle = Rectangle()

    private val resourceRect: Rectangle
        get() = rect.innerRect(innerMargin)

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        sizeUpdater?.invoke(virtualWidth, virtualHeight)?.let {
            rect = it
            boundedLabel?.rect = resourceRect
        }
    }

    override fun draw(virtualScreen: VirtualScreen) {
        if (!isVisible) return
        virtualScreen.draw(borderTextureName, rect)
        virtualScreen.draw(textAreaTextureName, resourceRect)
        boundedLabel?.draw()

    }

    override fun touchDown(virtualPoint: Point): Boolean = isVisible && rect.contains(virtualPoint)

    override fun touchUp(virtualPoint: Point): Boolean = isVisible && rect.contains(virtualPoint)

}