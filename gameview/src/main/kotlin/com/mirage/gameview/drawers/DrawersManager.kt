package com.mirage.gameview.drawers

import com.mirage.utils.game.objects.properties.Equipment
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.game.objects.simplified.SimplifiedEntity
import com.mirage.utils.game.states.SimplifiedState
import com.mirage.utils.game.states.StateDifference
import com.mirage.utils.virtualscreen.VirtualScreen

interface DrawersManager {

    fun getEntityDrawer(entityID: Long): Drawer?

    fun drawBuilding(buildingID: Long, virtualScreen: VirtualScreen, x: Float, y: Float, isOpaque: Boolean, currentTimeMillis: Long)

    fun drawEntity(entityID: Long, virtualScreen: VirtualScreen, x: Float, y: Float, isOpaque: Boolean, currentTimeMillis: Long, moveDirection: MoveDirection)

    fun loadDrawers(initialState: SimplifiedState, currentTimeMillis: Long = System.currentTimeMillis())

    fun updateDrawers(stateDifference: StateDifference, oldState: SimplifiedState, currentTimeMillis: Long = System.currentTimeMillis())

    fun updateEquipment(entityID: Long, entity: SimplifiedEntity, equipment: Equipment, currentTimeMillis: Long = System.currentTimeMillis())

}