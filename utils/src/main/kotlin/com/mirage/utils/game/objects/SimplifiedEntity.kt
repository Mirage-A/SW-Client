package com.mirage.utils.game.objects

import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.game.effects.Effect
import com.mirage.utils.game.objects.enums.MoveDirection

open class SimplifiedEntity(
        override val template: String = "",
        override val x: Float = 0f,
        override val y: Float = 0f,
        open val name: String = "",
        open val width: Float = 0f,
        open val height: Float = 0f,
        open val speed: Float = 0f,
        open val moveDirection: MoveDirection = MoveDirection.DOWN_RIGHT,
        open val isMoving: Boolean = false,
        open val state: String = "default",
        open val action: String = "IDLE",
        open val health: Float = 0f,
        open val maxHealth: Float = 0f,
        open val factionID: Int = 0,
        open val effects: Collection<Effect> = ArrayList()
) : SimplifiedObject {


    override fun simplifiedCopy(): SimplifiedEntity =
            SimplifiedEntity(template, x, y, name, width, height, speed, moveDirection, isMoving, state, action, health, maxHealth, factionID, effects)

    override fun with(template: String, x: Float, y: Float): SimplifiedEntity =
            SimplifiedEntity(template, x, y, name, width, height, speed, moveDirection, isMoving, state, action, health, maxHealth, factionID, effects)

    override val rectangle: Rectangle
        get() = Rectangle(x, y, width, height)

    override fun equals(other: Any?): Boolean {
        if (other !is SimplifiedEntity) return false
        return template == other.template &&
                x == other.x &&
                y == other.y &&
                name == other.name &&
                width == other.width &&
                height == other.height &&
                speed == other.speed &&
                moveDirection == other.moveDirection &&
                isMoving == other.isMoving &&
                state == other.state &&
                action == other.action &&
                health == other.health &&
                maxHealth == other.maxHealth &&
                factionID == other.factionID &&
                effects == other.effects
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String =
            "Entity(template=$template x=$x y=$y name=$name width=$width height=$height speed=$speed moveDirection=$moveDirection" +
                    " isMoving=$isMoving state=$state action=$action health=$health maxHealth=$maxHealth factionID=$factionID effects=$effects)"


}