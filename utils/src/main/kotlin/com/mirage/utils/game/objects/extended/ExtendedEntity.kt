package com.mirage.utils.game.objects.extended

import com.mirage.utils.game.effects.Effect
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.game.objects.simplified.SimplifiedEntity

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
        override var action: String = "IDLE",
        override var health: Float = 0f,
        override var maxHealth: Float = 0f,
        override var factionID: Int = 0,
        var isRigid: Boolean = false

) : ExtendedObject, SimplifiedEntity(template, x, y, name, width, height, speed, moveDirection, isMoving, state, action, health, maxHealth, factionID)