package com.mirage.view.gameobjects

import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Реализация [ObjectDrawer], которая хранит в себе 2 экземпляра [ObjectDrawer] и переключается между ними
 * при смене прозрачности.
 */
class OpaqueTransparentObjectDrawer(private val opaqueDrawer: ObjectDrawer, private val transparentDrawer: ObjectDrawer) : ObjectDrawer {

    /**
     * Является ли объект прозрачным
     */
    private var opacity : Boolean = true

    override fun setOpaque(isOpaque: Boolean) {
        opacity = isOpaque
    }

    override fun draw(batch: SpriteBatch, x: Float, y: Float) =
            if (opacity) opaqueDrawer.draw(batch, x, y)
            else transparentDrawer.draw(batch, x, y)
}