package com.mirage.view.drawers.templates

import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.VirtualScreen
import com.mirage.view.drawers.DrawerTemplate

class EmptyDrawerTemplate : DrawerTemplate {

    override fun draw(virtualScreen: VirtualScreen, x: Float, y: Float, width: Float, height: Float, isOpaque: Boolean, action: String, actionTimePassedMillis: Long, isMoving: Boolean, movingTimePassedMillis: Long, moveDirection: MoveDirection) {}

}