package com.mirage.view.drawers.templates

import com.mirage.view.drawers.DrawerTemplate
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.virtualscreen.VirtualScreen

class EmptyDrawerTemplate : DrawerTemplate {

    override fun draw(virtualScreen: VirtualScreen, x: Float, y: Float, isOpaque: Boolean, action: String, actionTimePassedMillis: Long, isMoving: Boolean, movingTimePassedMillis: Long, moveDirection: MoveDirection) {}

}