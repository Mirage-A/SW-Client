package com.mirage.view.drawers

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.game.states.StateDifference

interface DrawersManager {

    fun draw(objID: Long, batch: SpriteBatch, x: Float, y: Float, isOpaque: Boolean, currentTimeMillis: Long, moveDirection: GameObject.MoveDirection)

    fun loadDrawers(initialState: GameObjects, currentTimeMillis: Long = System.currentTimeMillis())

    fun updateDrawers(stateDifference: StateDifference, oldState: GameObjects, currentTimeMillis: Long = System.currentTimeMillis())

    fun updateEquipment(objID: Long, obj: GameObject, equipment: GameObject.HumanoidEquipment, currentTimeMillis: Long = System.currentTimeMillis())

}