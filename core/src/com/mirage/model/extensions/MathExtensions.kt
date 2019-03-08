package com.mirage.model.extensions

import com.badlogic.gdx.math.Rectangle
import com.mirage.model.datastructures.Point

val Rectangle.points : Array<Point>
    get() = arrayOf(Point(x, y), Point(x + width, y),
            Point(x, y + height), Point(x + width, y + height))

