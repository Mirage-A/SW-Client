package com.mirage.view.drawers

import com.mirage.core.datastructures.Rectangle
import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.game.objects.simplified.SimplifiedEntity
import com.mirage.core.game.states.SimplifiedState
import com.mirage.core.game.states.StateDifference
import com.mirage.core.virtualscreen.VirtualScreen

interface DrawersManager {

    fun getEntityHitbox(entityID: Long): Rectangle?

    fun drawBuilding(buildingID: Long, virtualScreen: VirtualScreen, x: Float, y: Float, isOpaque: Boolean, currentTimeMillis: Long)

    fun drawEntity(entityID: Long, virtualScreen: VirtualScreen, x: Float, y: Float, isOpaque: Boolean, currentTimeMillis: Long, moveDirection: MoveDirection)

    fun loadDrawers(initialState: SimplifiedState, currentTimeMillis: Long = System.currentTimeMillis())

    fun updateDrawers(stateDifference: StateDifference, oldState: SimplifiedState, currentTimeMillis: Long = System.currentTimeMillis())

    fun updateEquipment(entityID: Long, entity: SimplifiedEntity, equipment: Equipment, currentTimeMillis: Long = System.currentTimeMillis())

}