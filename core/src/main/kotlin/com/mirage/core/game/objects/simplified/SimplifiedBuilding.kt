package com.mirage.core.game.objects.simplified

import com.mirage.core.datastructures.Rectangle

open class SimplifiedBuilding(
        override val template: String = "",
        override val x: Float = 0f,
        override val y: Float = 0f,
        open val name: String = "",
        open val width: Float = 0f,
        open val height: Float = 0f,
        open val transparencyRange: Float = 0f,
        open val state: String = "default"
) : SimplifiedObject {


    override fun simplifiedCopy(): SimplifiedBuilding =
            SimplifiedBuilding(template, x, y, name, width, height, transparencyRange, state)

    override fun with(template: String, x: Float, y: Float): SimplifiedBuilding =
            SimplifiedBuilding(template, x, y, name, width, height, transparencyRange, state)

    fun with(
            template: String = this.template,
            x: Float = this.x,
            y: Float = this.y,
            name: String = this.name,
            width: Float = this.width,
            height: Float = this.height,
            transparencyRange: Float = this.transparencyRange,
            state: String = this.state
    ): SimplifiedBuilding = SimplifiedBuilding(template, x, y, name, width, height, transparencyRange, state)

    override val rectangle: Rectangle
        get() = Rectangle(x, y, width, height)

    override fun equals(other: Any?): Boolean {
        if (other !is SimplifiedBuilding) return false
        return template == other.template &&
                x == other.x &&
                y == other.y &&
                name == other.name &&
                width == other.width &&
                height == other.height &&
                transparencyRange == other.transparencyRange &&
                state == other.state
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String =
            "Building(template=$template x=$x y=$y name=$name width=$width height=$height transparencyRange=$transparencyRange" +
                    " state=$state)"

}