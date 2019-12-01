package com.mirage.gameview.drawers

import com.mirage.utils.game.oldobjects.GameObject
import com.mirage.utils.game.oldobjects.GameObjects
import com.mirage.utils.virtualscreen.VirtualScreen

interface DrawersManager {

    fun draw(objID: Long, virtualScreen: VirtualScreen, x: Float, y: Float, isOpaque: Boolean, currentTimeMillis: Long, moveDirection: GameObject.MoveDirection)

    fun loadDrawers(initialState: GameObjects, currentTimeMillis: Long = System.currentTimeMillis())

    fun updateDrawers(stateDifference: StateDifference, oldState: GameObjects, currentTimeMillis: Long = System.currentTimeMillis())

    fun updateEquipment(objID: Long, obj: GameObject, equipment: GameObject.HumanoidEquipment, currentTimeMillis: Long = System.currentTimeMillis())

}