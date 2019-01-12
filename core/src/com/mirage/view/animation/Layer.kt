package com.mirage.view.animation

import com.mirage.model.scene.Point
import com.mirage.view.animation.MoveDirection
import java.awt.image.BufferedImage
import java.io.File

import javax.imageio.ImageIO

/**
 * Слой на кадре анимации
 */
class Layer (var imageName: String, var x : Float = 0f, var y : Float = 0f, var scale : Float = 1f, var scaleX : Float = 1f,
             var scaleY : Float = 1f, var angle : Float = 0f, var basicWidth: Int = 0, var basicHeight: Int = 0) {
    /**
     * Размеры изображения слоя до скалирования
     */

    constructor(origin : Layer) : this(origin.imageName, origin.x, origin.y, origin.scale, origin.scaleX, origin.scaleY,
            origin.angle)


    /**
     * Обрезает формат изображения и возвращает название слоя
     */
    fun getName() = imageName.substring(0, imageName.length - 4)

    /**
     * Возвращает точку - координаты слоя
     */
    fun getPosition() : Point {
        return Point(x, y)
    }

}
