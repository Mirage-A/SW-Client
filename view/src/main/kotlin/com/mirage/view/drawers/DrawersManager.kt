package com.mirage.view.drawers

import com.mirage.core.utils.Rectangle
import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.game.objects.SimplifiedEntity
import com.mirage.core.game.objects.SimplifiedState
import com.mirage.core.game.objects.StateDifference
import com.mirage.core.VirtualScreen

interface DrawersManager {

    fun getEntityHitbox(entityID: Long): Rectangle?

    fun drawBuilding(buildingID: Long, virtualScreen: VirtualScreen, x: Float, y: Float, width: Float, height: Float, isOpaque: Boolean, currentTimeMillis: Long)

    fun drawEntity(entityID: Long, virtualScreen: VirtualScreen, x: Float, y: Float, width: Float, height: Float, isOpaque: Boolean, currentTimeMillis: Long, moveDirection: MoveDirection)

    fun loadDrawers(initialState: SimplifiedState, currentTimeMillis: Long = System.currentTimeMillis())

    fun updateDrawers(stateDifference: StateDifference, oldState: SimplifiedState, currentTimeMillis: Long = System.currentTimeMillis())

    fun updateEquipment(entityID: Long, entity: SimplifiedEntity, equipment: Equipment, currentTimeMillis: Long = System.currentTimeMillis())

}