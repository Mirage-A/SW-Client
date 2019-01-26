package com.mirage.view.gameobjects

import com.badlogic.gdx.graphics.Texture

/**
 * Статичная текстура, не изменяющаяся со временем
 */
class StaticTexture(private var texture: Texture) : Image() {

    override fun getTexture(): Texture {
        return texture
    }

    fun setTexture(texture: Texture) {
        this.texture = texture
    }
}
