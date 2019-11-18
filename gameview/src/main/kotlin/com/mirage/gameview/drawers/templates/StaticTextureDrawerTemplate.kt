package com.mirage.gameview.drawers.templates

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.utils.Assets
import com.mirage.utils.game.objects.GameObject
import com.mirage.gameview.drawers.DrawerTemplate

/**
 * Представление объекта как одной статичной текстуры.
 */
class StaticTextureDrawerTemplate(textureName: String) : DrawerTemplate {

    private val texture = Assets.getRawTexture("objects/$textureName")

    /**
     * Отрисовка текстуры.
     * Центр текстуры совпадает с точкой (x, y).
     */
    override fun draw(batch: SpriteBatch, x: Float, y: Float, isOpaque: Boolean, action: String, actionTimePassedMillis: Long, isMoving: Boolean, movingTimePassedMillis: Long, moveDirection: GameObject.MoveDirection) {
        if (isOpaque) batch.draw(texture, x - texture.width / 2, y - texture.height / 2)
    }

}