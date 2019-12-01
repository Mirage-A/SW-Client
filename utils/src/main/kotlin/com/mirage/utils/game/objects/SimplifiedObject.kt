package com.mirage.utils.game.objects

import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle

interface SimplifiedObject {

    val template: String

    val x: Float

    val y: Float

    /**
     * Создаёт упрощённую копию объекта.
     */
    fun simplifiedCopy() : SimplifiedObject

    /**
     * Создаёт копию этого объекта с измененными параметрами.
     */
    fun with(
            template: String = this.template,
            x: Float = this.x,
            y: Float = this.y
    ) : SimplifiedObject

    val position: Point
        get() = Point(x, y)

    val rectangle: Rectangle

}