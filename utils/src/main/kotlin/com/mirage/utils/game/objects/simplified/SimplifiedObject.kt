package com.mirage.utils.game.objects.simplified

import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle

interface SimplifiedObject {

    val template: String

    val x: Float

    val y: Float

    /** Creates a simplified copy of an object */
    fun simplifiedCopy() : SimplifiedObject

    /** Creates a copy of this object with changed parameters */
    fun with(
            template: String = this.template,
            x: Float = this.x,
            y: Float = this.y
    ) : SimplifiedObject

    val position: Point
        get() = Point(x, y)

    val rectangle: Rectangle

}