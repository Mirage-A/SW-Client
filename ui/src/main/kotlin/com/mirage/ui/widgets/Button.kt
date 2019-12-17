package com.mirage.ui.widgets

import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.virtualscreen.VirtualScreen

/**
 * @param textureName Текстура кнопки по умолчанию.
 * @param highlightedTextureName Текстура кнопки при наведении на неё.
 * @param pressedTextureName Текстура кнопки при нажатии на неё.
 * @param rect Позиция кнопки на виртуальном экране.
 * @param boundedLabel Текстовое поле, которое будет привязано к кнопке. Размеры поля будут равны размеру кнопки.
 * @param sizeUpdater Функция, вызывающаяся для определения позиции кнопки при изменении размера виртуального экрана.
 * @param onPressed Функция, вызывающаяся при нажатии кнопки.
 */
class Button(
        var textureName: String,
        var highlightedTextureName: String = textureName,
        var pressedTextureName: String = highlightedTextureName,
        var rect: Rectangle = Rectangle(),
        var boundedLabel: VirtualScreen.Label? = null,
        var sizeUpdater: ((Float, Float) -> Rectangle)? = null,
        var onPressed: () -> Unit = {},
        var borderSize: Float = 0f,
        var borderTextureName: String = "ui/btn-border"
) : Widget {
    var isPressed = false
    var isHighlighted = false
    var isVisible = true
    var keyPressed = false // Для случаев, когда кнопка может нажиматься как курсором, так и с клавиатуры.

    private val innerRect: Rectangle
        get() = Rectangle(rect.x, rect.y, rect.width - borderSize * 2f, rect.height - borderSize * 2f)

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
            rect = it
            boundedLabel?.rect = innerRect
            boundedLabel?.resizeFont(virtualWidth, virtualHeight)
        }
    }

    override fun touchUp(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        isPressed = false
        if (rect.contains(virtualPoint)) {
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
        isPressed = rect.contains(virtualPoint)
        isHighlighted = isPressed
        return isPressed
    }

    override fun mouseMoved(virtualPoint: Point) {
        if (!isVisible) return
        isHighlighted = rect.contains(virtualPoint)
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