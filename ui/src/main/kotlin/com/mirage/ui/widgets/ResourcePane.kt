package com.mirage.ui.widgets

import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.virtualscreen.VirtualScreen
import kotlin.math.max
import kotlin.math.min

class ResourcePane(
        var borderTextureName: String,
        var lostResourceTextureName: String,
        var resourceTextureName: String,
        var rect: Rectangle = Rectangle(),
        var boundedLabel: VirtualScreen.Label? = null,
        var innerMargin: Float,
        var sizeUpdater: ((Float, Float) -> Rectangle)? = null
) : Widget {

    var isHighlighted = false
    var isVisible = true

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

    override fun mouseMoved(virtualPoint: Point) {
        if (!isVisible) return
        isHighlighted = rect.contains(virtualPoint)
    }

    fun draw(virtualScreen: VirtualScreen, currentResource: Int, maxResource: Int) {
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

    override fun touchDown(virtualPoint: Point): Boolean = rect.contains(virtualPoint)

    override fun touchUp(virtualPoint: Point): Boolean = rect.contains(virtualPoint)

}