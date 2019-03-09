package com.mirage.view.gameobjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Обёртка для текстуры
 * Может использоваться как наследник ObjectDrawer
 */
abstract class Image(private val alignment: Alignment = Alignment.BOTTOM_LEFT) : ObjectDrawer {

    /**
     * Расположение переданной в метод draw точки относительно текстуры
     */
    enum class Alignment {
        LEFT,
        RIGHT,
        CENTER,
        TOP,
        BOTTOM,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        TOP_LEFT,
        TOP_RIGHT
    }

    override fun draw(batch: SpriteBatch, x: Float, y: Float) {
        val texture = getTexture()
        val textureX = when (alignment) {
            Alignment.LEFT, Alignment.BOTTOM_LEFT, Alignment.TOP_LEFT -> x
            Alignment.RIGHT, Alignment.BOTTOM_RIGHT, Alignment.TOP_RIGHT -> x - texture.width
            Alignment.CENTER, Alignment.BOTTOM, Alignment.TOP -> x - texture.width / 2
        }
        val textureY = when (alignment) {
            Alignment.TOP, Alignment.TOP_LEFT, Alignment.TOP_RIGHT -> y
            Alignment.BOTTOM, Alignment.BOTTOM_LEFT, Alignment.BOTTOM_RIGHT -> y - texture.height
            Alignment.CENTER, Alignment.LEFT, Alignment.RIGHT -> y - texture.height / 2
        }
        batch.draw(texture, textureX, textureY)
    }

    abstract fun getTexture(): Texture
}
