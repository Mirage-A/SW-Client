package com.mirage.logic.state

import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.game.objects.SimplifiedEntity

class ExtendedEntity(
        override var template: String = "",
        override var x: Float = 0f,
        override var y: Float = 0f,
        override var name: String = "",
        override var width: Float = 0f,
        override var height: Float = 0f,
        override var speed: Float = 0f,
        override var moveDirection: MoveDirection = MoveDirection.DOWN_RIGHT,
        override var isMoving: Boolean = false,
        override var state: String = "default",
        override var action: String = "idle",
        override var health: Int = 0,
        override var maxHealth: Int = 0,
        override var factionID: Int = 0,
        override var interactionRange: Float = 0f,
        var isRigid: Boolean = false

) : ExtendedObject, SimplifiedEntity(template, x, y, name, width, height, speed, moveDirection, isMoving, state, action, health, maxHealth, factionID, interactionRange) {

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
            interactionRange: Float = this.interactionRange,
            isRigid: Boolean = this.isRigid
    ): ExtendedEntity =
            ExtendedEntity(template, x, y, name, width, height, speed, moveDirection, isMoving, state, action, health, maxHealth, factionID, interactionRange, isRigid)

    override fun with(template: String, x: Float, y: Float): ExtendedEntity = with(
            template = template,
            x = x,
            y = y,
            isRigid = this.isRigid
    )

}