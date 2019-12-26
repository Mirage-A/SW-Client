package com.mirage.ui.widgets

import com.mirage.core.datastructures.Point
import com.mirage.core.datastructures.Rectangle
import com.mirage.core.virtualscreen.VirtualScreen
import kotlin.math.max
import kotlin.math.min

internal class ResourcePane(
        var borderTextureName: String = "ui/game/health-border",
        var lostResourceTextureName: String = "ui/game/health-lost",
        var resourceTextureName: String = "ui/game/health",
        var boundedLabel: VirtualScreen.Label? = null,
        var innerMargin: Float = 0f,
        var sizeUpdater: ((Float, Float) -> Rectangle)? = null
) : Widget {

    private var isHighlighted = false
    override var isVisible = true

    var currentResource: Int = 0
    var maxResource: Int = 0

    private var rect: Rectangle = Rectangle()

    private val resourceRect: Rectangle
        get() = rect.innerRect(innerMargin)

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        sizeUpdater?.invoke(virtualWidth, virtualHeight)?.let {
            rect = it
            boundedLabel?.rect = resourceRect
        }
    }

    override fun mouseMoved(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        isHighlighted = rect.contains(virtualPoint)
        return isHighlighted
    }

    override fun draw(virtualScreen: VirtualScreen) {
        if (!isVisible) return
        val currentWidth = resourceRect.width * min(currentResource, maxResource).toFloat() / max(maxResource, 1).toFloat()
        val currentResourceRect = Rectangle(resourceRect.leftX + currentWidth / 2f, resourceRect.y, currentWidth, resourceRect.height)
        virtualScreen.draw(borderTextureName, rect)
        virtualScreen.draw(lostResourceTextureName, resourceRect)
        virtualScreen.draw(resourceTextureName, currentResourceRect)
        if (isHighlighted) {
            boundedLabel?.text = "$currentResource/$maxResource"
            boundedLabel?.draw()
        }
    }

    override fun touchDown(virtualPoint: Point): Boolean = isVisible && rect.contains(virtualPoint)

    override fun touchUp(virtualPoint: Point): Boolean = isVisible && rect.contains(virtualPoint)

}