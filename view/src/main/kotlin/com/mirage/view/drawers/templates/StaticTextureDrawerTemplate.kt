package com.mirage.view.drawers.templates

import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.view.drawers.DrawerTemplate

/** Visual representation of an object as a static texture */
class StaticTextureDrawerTemplate(textureName: String) : DrawerTemplate {

    private val texture = "objects/$textureName"

    /** Draws [texture] with center at point ([x], [y]) */
    override fun draw(virtualScreen: VirtualScreen,
                      x: Float,
                      y: Float,
                      isOpaque: Boolean,
                      action: String,
                      actionTimePassedMillis: Long,
                      isMoving: Boolean,
                      movingTimePassedMillis: Long,
                      moveDirection: MoveDirection
    ) {
        if (isOpaque) virtualScreen.draw(texture, x, y)
    }

    override fun toString(): String = "StaticTextureDrawerTemplate(texture=$texture)"

}