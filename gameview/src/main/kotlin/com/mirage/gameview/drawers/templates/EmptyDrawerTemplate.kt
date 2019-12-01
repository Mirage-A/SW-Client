package com.mirage.gameview.drawers.templates

import com.mirage.gameview.drawers.DrawerTemplate
import com.mirage.utils.game.oldobjects.GameObject
import com.mirage.utils.virtualscreen.VirtualScreen

class EmptyDrawerTemplate : DrawerTemplate {
    override fun draw(virtualScreen: VirtualScreen, x: Float, y: Float, isOpaque: Boolean, action: String, actionTimePassedMillis: Long, isMoving: Boolean, movingTimePassedMillis: Long, moveDirection: GameObject.MoveDirection) {}

}