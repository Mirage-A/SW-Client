package com.mirage.utils.game.objects.simplified

import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.game.effects.Effect
import com.mirage.utils.game.objects.properties.MoveDirection

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
        open val action: String = "idle",
        open val health: Int = 0,
        open val maxHealth: Int = 0,
        open val factionID: Int = 0,
        open val interactionRange: Float = 0f
) : SimplifiedObject {


    override fun simplifiedCopy(): SimplifiedEntity =
            SimplifiedEntity(template, x, y, name, width, height, speed, moveDirection, isMoving, state, action, health, maxHealth, factionID, interactionRange)

    override fun with(template: String, x: Float, y: Float): SimplifiedEntity =
            SimplifiedEntity(template, x, y, name, width, height, speed, moveDirection, isMoving, state, action, health, maxHealth, factionID, interactionRange)

    fun with(
            template: String = this.template,
            x: Float = this.x,
            y: Float = this.y,
            name: String = this.name,
            width: Float = this.width,
            height: Float = this.height,
            speed: Float = this.speed,
            moveDirection: MoveDirection = this.moveDirection,
            isMoving: Boolean = this.isMoving,
            state: String = this.state,
            action: String = this.action,
            health: Int = this.health,
            maxHealth: Int = this.maxHealth,
            factionID: Int = this.factionID,
            interactionRange: Float = this.interactionRange
    ): SimplifiedEntity =
            SimplifiedEntity(template, x, y, name, width, height, speed, moveDirection, isMoving, state, action, health, maxHealth, factionID, interactionRange)

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
                interactionRange == other.interactionRange
    }

    override fun hashCode(): Int {
        //TODO
        return super.hashCode()
    }

    override fun toString(): String =
            "Entity(template=$template x=$x y=$y name=$name width=$width height=$height speed=$speed moveDirection=$moveDirection" +
                    " isMoving=$isMoving state=$state action=$action health=$health maxHealth=$maxHealth factionID=$factionID interactionRange=$interactionRange)"


}