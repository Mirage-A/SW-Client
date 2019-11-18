package com.mirage.utils.extensions

import com.badlogic.gdx.math.Rectangle
import com.mirage.utils.datastructures.MutablePoint
import kotlin.math.roundToInt

val Rectangle.points : Array<MutablePoint>
    get() = arrayOf(MutablePoint(x, y), MutablePoint(x + width, y),
            MutablePoint(x, y + height), MutablePoint(x + width, y + height))


fun sqr(a: Float) = a * a

fun Float.trunc() : Int {
    val a = this.roundToInt()
    return if (this >= a.toFloat()) a
    else a - 1
}