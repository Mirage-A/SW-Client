package com.mirage.gameview.drawers

import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.virtualscreen.VirtualScreen

/**
 * Представление шаблонного объекта в одном из его состояний.
 */
interface DrawerTemplate {

    fun draw(virtualScreen: VirtualScreen, x: Float, y: Float, isOpaque: Boolean,
             action: String, actionTimePassedMillis: Long,
             isMoving: Boolean, movingTimePassedMillis: Long, moveDirection: GameObject.MoveDirection)

}