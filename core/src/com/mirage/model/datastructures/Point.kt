package com.mirage.model.datastructures

/**
 * Точка с координатами типа Float
 * Используется для хранения позиции объектов на сцене, поэтому имеет метод move
 */
data class Point (var x : Float = 0f, var y : Float = 0f){

    /**
     * Передвигает точку в сторону заданного угла на заданное расстояние
     */
    fun move(angle: Float, range: Float) {
        x += (Math.cos(angle.toDouble()) * range).toFloat()
        y += (Math.sin(angle.toDouble()) * range).toFloat()
    }

    operator fun plus(p: Point) = Point(x + p.x, y + p.y)

    operator fun minus(p: Point) = Point(x - p.x, y - p.y)

    override fun toString(): String {
        return "[$x, $y]"
    }

}