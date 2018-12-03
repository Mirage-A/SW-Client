package com.mirage.model.scene

class Point (var x : Float, var y : Float){

    override fun toString(): String {
        return "[$x, $y]"
    }

    override fun equals(other: Any?): Boolean {
        if (other is Point) {
            return (x == other.x && y == other.y)
        }
        return false
    }
}