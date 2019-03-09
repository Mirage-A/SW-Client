package com.mirage.gamelogic.extensions

import com.badlogic.gdx.math.Rectangle
import com.mirage.gamelogic.datastructures.Point

val Rectangle.points : Array<Point>
    get() = arrayOf(Point(x, y), Point(x + width, y),
            Point(x, y + height), Point(x + width, y + height))

