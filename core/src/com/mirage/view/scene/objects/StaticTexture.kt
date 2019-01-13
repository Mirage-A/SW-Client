package com.mirage.view.scene.objects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Объект без анимации, заданный статичной текстурой
 */
class StaticTexture(private val texture: Texture) : ObjectDrawer() {

    override fun draw(batch: SpriteBatch, x: Float, y: Float) {
        batch.draw(texture, x, y)
    }
}
