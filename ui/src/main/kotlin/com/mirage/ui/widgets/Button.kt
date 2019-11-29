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
        var onPressed: () -> Unit = {}
) {
    var isPressed = false
    var isHighlighted = false

    init {
        boundedLabel?.rect = rect
    }

    fun getCurrentTextureName() =
            when {
                isPressed -> pressedTextureName
                isHighlighted -> highlightedTextureName
                else -> textureName
            }

    fun resize(virtualWidth: Float, virtualHeight: Float) {
        sizeUpdater?.invoke(virtualWidth, virtualHeight)?.let {
            boundedLabel?.rect = it
            rect = it
        }
    }

    fun touchUp(virtualPoint: Point) {
        if (rect.contains(virtualPoint)) {
            onPressed()
        }
        else {
            isHighlighted = false
        }
        isPressed = false
    }

    fun touchDown(virtualPoint: Point) {
        isPressed = rect.contains(virtualPoint)
        isHighlighted = isPressed
    }

    fun mouseMoved(virtualPoint: Point) {
        isHighlighted = rect.contains(virtualPoint)
    }

    fun draw(virtualScreen: VirtualScreen) {
        virtualScreen.draw(getCurrentTextureName(), rect)
        boundedLabel?.draw()
    }
}