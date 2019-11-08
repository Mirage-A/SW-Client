package com.mirage.utils.extensions

import com.badlogic.gdx.math.Rectangle
import com.mirage.utils.datastructures.MutablePoint

val Rectangle.points : Array<MutablePoint>
    get() = arrayOf(MutablePoint(x, y), MutablePoint(x + width, y),
            MutablePoint(x, y + height), MutablePoint(x + width, y + height))


fun sqr(a: Float) = a * a