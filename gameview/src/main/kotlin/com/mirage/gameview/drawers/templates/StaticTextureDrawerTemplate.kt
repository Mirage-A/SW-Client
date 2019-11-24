package com.mirage.gameview.drawers.templates

import com.mirage.gameview.drawers.DrawerTemplate
import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.virtualscreen.VirtualScreen

/**
 * Представление объекта как одной статичной текстуры.
 */
class StaticTextureDrawerTemplate(textureName: String) : DrawerTemplate {

    private val texture = "objects/$textureName"

    /**
     * Отрисовка текстуры.
     * Центр текстуры совпадает с точкой (x, y).
     */
    override fun draw(virtualScreen: VirtualScreen,
                      x: Float,
                      y: Float,
                      isOpaque: Boolean,
                      action: String,
                      actionTimePassedMillis: Long,
                      isMoving: Boolean,
                      movingTimePassedMillis: Long,
                      moveDirection: GameObject.MoveDirection
    ) {
        if (isOpaque) virtualScreen.draw(texture, x, y)
    }

}