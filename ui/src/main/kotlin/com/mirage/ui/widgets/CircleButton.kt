package com.mirage.ui.widgets

import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.datastructures.rangeBetween
import com.mirage.utils.virtualscreen.VirtualScreen

class CircleButton(
        var textureName: String,
        var highlightedTextureName: String = textureName,
        var pressedTextureName: String = highlightedTextureName,
        var center: Point = Point(0f, 0f),
        var radius: Float = 0f,
        var boundedLabel: VirtualScreen.Label? = null,
        var sizeUpdater: ((Float, Float) -> Pair<Point, Float>)? = null,
        var onPressed: () -> Unit = {},
        var borderSize: Float = 0f,
        var borderTextureName: String = "ui/circle-border"
) : Widget {

    var isPressed = false
    var isHighlighted = false
    var isVisible = true
    var keyPressed = false // Для случаев, когда кнопка может нажиматься как курсором, так и с клавиатуры.

    private val rect: Rectangle
        get() = Rectangle(center.x, center.y, radius * 2f, radius * 2f)

    private val innerRect: Rectangle
        get() = Rectangle(center.x, center.y, (radius - borderSize) * 2f, (radius - borderSize) * 2f)

    init {
        boundedLabel?.rect = innerRect
    }

    private fun getCurrentTextureName() =
            when {
                isPressed || keyPressed -> pressedTextureName
                isHighlighted -> highlightedTextureName
                else -> textureName
            }

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        sizeUpdater?.invoke(virtualWidth, virtualHeight)?.let {
            center = it.first
            radius = it.second
            boundedLabel?.rect = innerRect
        }
    }

    override fun touchUp(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        isPressed = false
        if (rangeBetween(center, virtualPoint) < radius) {
            onPressed()
            return true
        }
        else {
            isHighlighted = false
        }
        return false
    }

    override fun touchDown(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        isPressed = rangeBetween(center, virtualPoint) < radius
        isHighlighted = isPressed
        return isPressed
    }

    override fun mouseMoved(virtualPoint: Point) {
        if (!isVisible) return
        isHighlighted = rangeBetween(center, virtualPoint) < radius
    }

    override fun draw(virtualScreen: VirtualScreen) {
        if (!isVisible) return
        if (borderSize != 0f) virtualScreen.draw(borderTextureName, rect)
        virtualScreen.draw(getCurrentTextureName(), innerRect)
        boundedLabel?.draw()
    }

    override fun unpress() {
        isPressed = false
    }

}