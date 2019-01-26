package com.mirage.view.scene.objects.humanoid

import com.badlogic.gdx.graphics.Texture
import com.mirage.view.scene.objects.Image

/**
 * Статичная текстура, не изменяющаяся со временем
 */
class StaticTexture(private var t: Texture) : Image() {

    override fun getTexture(): Texture {
        return t
    }

    fun setTexture(texture: Texture) {
        t = texture
    }
}
