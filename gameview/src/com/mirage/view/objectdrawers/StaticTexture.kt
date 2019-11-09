package com.mirage.view.objectdrawers

import com.badlogic.gdx.graphics.Texture

/**
 * Статичная текстура, не изменяющаяся со временем
 */
class StaticTexture(private var texture: Texture, alignment: Alignment = Alignment.BOTTOM_LEFT) : Image(alignment) {

    override fun getTexture(): Texture {
        return texture
    }

    fun setTexture(texture: Texture) {
        this.texture = texture
    }
}
