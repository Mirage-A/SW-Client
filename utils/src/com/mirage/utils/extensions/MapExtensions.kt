package com.mirage.utils.extensions

import com.badlogic.gdx.math.Rectangle
import com.mirage.utils.datastructures.Point
import com.mirage.utils.gameobjects.GameObject

val GameObject.rectangle : Rectangle
    get() = Rectangle(
            x - width / 2, y - height / 2, x + width / 2, y + height / 2
    )

val GameObject.position : Point
    get() = Point(x, y)
