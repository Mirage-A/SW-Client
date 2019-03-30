package com.mirage.utils.datastructures

/**
 * Неизменяемая точка с координатами типа Float
 * Используется для хранения позиции объектов на сцене
 */
data class Point (val x: Float, val y: Float){

    /**
     * Создаёт точку, сдвинутую на заданное расстояние в сторону заданного угла
     */
    fun move(angle: Float, range: Float) : Point =
            Point(x + (Math.cos(angle.toDouble()) * range).toFloat(), y + (Math.sin(angle.toDouble()) * range).toFloat())

    operator fun plus(p: Point) = Point(x + p.x, y + p.y)

    operator fun minus(p: Point) = Point(x - p.x, y - p.y)

    override fun toString(): String {
        return "[$x, $y]"
    }

}