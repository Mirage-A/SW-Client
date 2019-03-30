package com.mirage.view.animation

import com.mirage.utils.datastructures.MutablePoint

/**
 * Слой на кадре анимации
 */
class Layer (var imageName: String, var x : Float = 0f, var y : Float = 0f, var scale : Float = 1f, var scaleX : Float = 1f,
             var scaleY : Float = 1f, var angle : Float = 0f, var basicWidth: Int = 0, var basicHeight: Int = 0) {
    /**
     * Обрезает формат изображения и возвращает название слоя
     */
    fun getName() = imageName.substring(0, imageName.length - 4)

    /**
     * Возвращает точку - координаты слоя
     */
    fun getPosition() : MutablePoint {
        return MutablePoint(x, y)
    }

}
