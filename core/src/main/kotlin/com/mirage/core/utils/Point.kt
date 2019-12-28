package com.mirage.core.datastructures

import com.mirage.core.extensions.sqr
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Неизменяемая точка с координатами типа Float
 * Используется для хранения позиции объектов на сцене
 */
data class Point(val x: Float, val y: Float) {

    /**
     * Создаёт точку, сдвинутую на заданное расстояние в сторону заданного угла
     */
    fun move(angle: Float, range: Float): Point =
            Point(x + (cos(angle.toDouble()) * range).toFloat(), y + (sin(angle.toDouble()) * range).toFloat())

    operator fun plus(p: Point) = Point(x + p.x, y + p.y)

    operator fun minus(p: Point) = Point(x - p.x, y - p.y)

    override fun toString(): String {
        return "[$x, $y]"
    }

    operator fun rangeTo(p: Point): Float = sqrt(sqr(p.x - this.x) + sqr(p.y - this.y))

    operator fun times(scale: Float): Point = Point(x * scale, y * scale)

    /**
     * Проверяет, что данная точка находится на расстоянии менее 0.1f от точки [point].
     * Используется для тестов.
     */
    infix fun near(point: Point): Boolean = rangeBetween(this, point) < 0.1f


}

fun rangeBetween(p: Point, other: Point): Float = p..other
