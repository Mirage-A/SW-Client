package com.mirage.utils.datastructures

/**
 * Прямоугольник с центром в точке (x, y) и размерами width x height
 */
data class Rectangle(val x: Float = 0f, val y: Float = 0f, val width: Float = 0f, val height: Float = 0f) {

    val leftX : Float
        get() = x - width / 2
    val rightX : Float
        get() = x + width / 2
    val bottomY : Float
        get() = y - height / 2
    val topY : Float
        get() = y + height / 2
    val points: Iterable<Point>
        get() = listOf(
                Point(leftX, bottomY),
                Point(leftX, topY),
                Point(rightX, topY),
                Point(rightX, bottomY)
        )


    /**
     * Проверяет два прямоугольника на строгое пересечение
     * (Пересечение по границе не считается)
     */
    fun overlaps(other: Rectangle) : Boolean =
            (x - width / 2 < other.x + other.width / 2) &&
                    (x + width / 2 > other.x - other.width / 2) &&
                    (y - height / 2 < other.y + other.height / 2) &&
                    (y + height / 2 > other.y - other.height / 2)

    /**
     * Проверяет, содержится ли точка внутри прямоугольника
     */
    fun contains(p: Point) : Boolean =
            p.x < rightX && p.x > leftX && p.y < topY && p.y > bottomY


}