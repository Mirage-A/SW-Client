package com.mirage.view.drawers

import com.mirage.core.utils.Rectangle
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.VirtualScreen

/**
 * Visual representation of a game object in one of its states.
 * Unlike [DrawerTemplate] it bounds to a concrete object - for example, it stores time of latest update of an object's state.
 */
interface Drawer {

    fun draw(virtualScreen: VirtualScreen,
             x: Float,
             y: Float,
             width: Float,
             height: Float,
             isOpaque: Boolean,
             currentTimeMillis: Long,
             moveDirection: MoveDirection = MoveDirection.DOWN_RIGHT
    )

    fun setAction(newAction: String, currentTimeMillis: Long = System.currentTimeMillis())

    fun setMoving(isMoving: Boolean, currentTimeMillis: Long = System.currentTimeMillis())

    val hitBox: Rectangle

}