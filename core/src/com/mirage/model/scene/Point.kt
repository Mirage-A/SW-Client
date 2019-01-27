package com.mirage.model.scene

class Point (var x : Float = 0f, var y : Float = 0f){

    /**
     * Передвигает точку в сторону заданного угла на заданное расстояние
     */
    fun move(angle: Float, range: Float) {
        x += (Math.cos(angle.toDouble()) * range).toFloat()
        y += (Math.sin(angle.toDouble()) * range).toFloat()
    }

    override fun toString(): String {
        return "[$x, $y]"
    }

    override fun equals(other: Any?): Boolean {
        if (other is Point) {
            return (x == other.x && y == other.y)
        }
        return false
    }

    override fun hashCode(): Int {
        return x.hashCode() / (y.hashCode() % 4 + 1) + y.hashCode() * (x.hashCode() % 5)
    }

}