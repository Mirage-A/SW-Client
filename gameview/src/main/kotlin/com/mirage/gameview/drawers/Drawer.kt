package com.mirage.gameview.drawers

import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.virtualscreen.VirtualScreen

/**
 * Visual representation of a game object in one of its states.
 * Unlike [DrawerTemplate] it bounds to a concrete object - for example, it stores time of latest update of an object's state.
 */
interface Drawer {

    fun draw(virtualScreen: VirtualScreen,
             x: Float,
             y: Float,
             isOpaque: Boolean,
             currentTimeMillis: Long,
             moveDirection: MoveDirection = MoveDirection.DOWN_RIGHT
    )

    fun setAction(newAction: String, currentTimeMillis: Long = System.currentTimeMillis())

    fun setMoving(isMoving: Boolean, currentTimeMillis: Long = System.currentTimeMillis())

    val hitBox: Rectangle

}