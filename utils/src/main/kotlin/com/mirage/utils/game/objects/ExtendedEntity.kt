package com.mirage.utils.game.objects

import com.mirage.utils.game.behaviors.Behavior
import com.mirage.utils.game.behaviors.EmptyBehavior
import com.mirage.utils.game.effects.Effect
import com.mirage.utils.game.oldobjects.GameObject

class ExtendedEntity(
        override var template: String = "",
        override var x: Float = 0f,
        override var y: Float = 0f,
        override var name: String = "",
        override var width: Float = 0f,
        override var height: Float = 0f,
        override var speed: Float = 0f,
        override var moveDirection: GameObject.MoveDirection = GameObject.MoveDirection.DOWN_RIGHT,
        override var isMoving: Boolean = false,
        override var state: String = "default",
        override var action: String = "IDLE",
        override var health: Float = 0f,
        override var maxHealth: Float = 0f,
        override var factionID: Int = 0,
        override var effects: MutableCollection<Effect> = ArrayList(),
        var isRigid: Boolean = false,
        var behavior: Behavior = EmptyBehavior()

) : ExtendedObject, SimplifiedEntity(template, x, y, name, width, height, speed, moveDirection, isMoving, state, action, health, maxHealth, factionID, effects)