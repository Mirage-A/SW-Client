package com.mirage.view.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Обёртка для текстуры
 * Может использоваться как наследник ObjectDrawer
 */
abstract class Image : ObjectDrawer() {

    override fun draw(batch: SpriteBatch, x: Float, y: Float) {
        batch.draw(getTexture(), x, y)
    }

    abstract fun getTexture(): Texture
}
