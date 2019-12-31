package com.mirage.core.utils

import kotlin.math.roundToInt


fun sqr(a: Float) = a * a

fun Float.trunc(): Int {
    val a = this.roundToInt()
    return if (this >= a.toFloat()) a
    else a - 1
}