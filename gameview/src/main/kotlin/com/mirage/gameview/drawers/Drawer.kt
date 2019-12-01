package com.mirage.gameview.drawers

import com.mirage.utils.game.oldobjects.GameObject
import com.mirage.utils.virtualscreen.VirtualScreen

/**
 * Представление конкретного объекта в одном из его состояний.
 * В отличие от [DrawerTemplate] хранит параметры, привязанные к конкретному объекту - например, время последней смены состояния.
 */
interface Drawer {

    fun draw(virtualScreen: VirtualScreen, x: Float, y: Float, isOpaque: Boolean, currentTimeMillis: Long, moveDirection: GameObject.MoveDirection)

    fun setAction(newAction: String, currentTimeMillis: Long = System.currentTimeMillis())

    fun setMoving(isMoving: Boolean, currentTimeMillis: Long = System.currentTimeMillis())

}