package com.mirage.view.drawers.templates

import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.VirtualScreen
import com.mirage.core.utils.Point
import com.mirage.view.drawers.DrawerTemplate

/** Visual representation of an object as a static texture */
class StaticTextureDrawerTemplate(textureName: String) : DrawerTemplate {

    private val texture = "objects/$textureName"

    /** Draws [texture] with smart position for an object */
    override fun draw(virtualScreen: VirtualScreen,
                      x: Float,
                      y: Float,
                      width: Float,
                      height: Float,
                      isOpaque: Boolean,
                      action: String,
                      actionTimePassedMillis: Long,
                      isMoving: Boolean,
                      movingTimePassedMillis: Long,
                      moveDirection: MoveDirection
    ) {
        if (isOpaque) virtualScreen.smartObjectDraw(texture, x, y, width, height)
    }

    override fun toString(): String = "StaticTextureDrawerTemplate(texture=$texture)"

}