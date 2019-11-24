package com.mirage.gameview.drawers

import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.virtualscreen.VirtualScreen

class DrawerImpl(private val drawerTemplate: DrawerTemplate, currentTimeMillis: Long = System.currentTimeMillis()) : Drawer {

    private var lastActionChangeTime: Long = currentTimeMillis
    private var action: String = ""

    private var isMoving: Boolean = false
    private var lastMovingChangeTime: Long = currentTimeMillis

    override fun draw(virtualScreen: VirtualScreen, x: Float, y: Float, isOpaque: Boolean, currentTimeMillis: Long, moveDirection: GameObject.MoveDirection) {
        drawerTemplate.draw(virtualScreen, x, y, isOpaque,
                action, currentTimeMillis - lastActionChangeTime,
                isMoving, currentTimeMillis - lastMovingChangeTime, moveDirection)
    }

    override fun setAction(newAction: String, currentTimeMillis: Long) {
        action = newAction
        lastActionChangeTime = currentTimeMillis
    }

    override fun setMoving(isMoving: Boolean, currentTimeMillis: Long) {
        this.isMoving = isMoving
        lastMovingChangeTime = currentTimeMillis
    }
}