package com.mirage.view.drawers

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.utils.game.objects.GameObject

/**
 * Представление шаблонного объекта в одном из его состояний.
 */
interface DrawerTemplate {

    fun draw(batch: SpriteBatch, x: Float, y: Float, isOpaque: Boolean,
             action: String, actionTimePassedMillis: Long,
             isMoving: Boolean, movingTimePassedMillis: Long, moveDirection: GameObject.MoveDirection)

}