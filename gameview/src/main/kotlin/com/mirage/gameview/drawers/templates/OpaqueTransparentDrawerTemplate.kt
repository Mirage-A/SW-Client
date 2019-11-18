package com.mirage.gameview.drawers.templates

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.utils.game.objects.GameObject
import com.mirage.gameview.drawers.DrawerTemplate

/**
 * Composite из двух представлений.
 * [opaqueDrawerTemplate] используется для отрисовки непрозрачного объекта.
 * [transparentDrawerTemplate] используется для отрисовки прозрачного объекта.
 * В качестве параметра isOpaque [transparentDrawerTemplate] получает true.
 * Не рекомендуется делать объект класса [OpaqueTransparentDrawerTemplate] одним из двух параметров конструктора.
 */
class OpaqueTransparentDrawerTemplate(
        private val opaqueDrawerTemplate: DrawerTemplate,
        private val transparentDrawerTemplate: DrawerTemplate
) : DrawerTemplate {

    override fun draw(batch: SpriteBatch, x: Float, y: Float, isOpaque: Boolean, action: String, actionTimePassedMillis: Long, isMoving: Boolean, movingTimePassedMillis: Long, moveDirection: GameObject.MoveDirection) {
        if (isOpaque) opaqueDrawerTemplate.draw(batch, x, y, true, action, actionTimePassedMillis, isMoving, movingTimePassedMillis, moveDirection)
        else transparentDrawerTemplate.draw(batch, x, y, true, action, actionTimePassedMillis, isMoving, movingTimePassedMillis, moveDirection)
    }

}