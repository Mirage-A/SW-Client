package com.mirage.view.scene.objects.humanoid

import com.badlogic.gdx.graphics.Texture

/**
 * Статичная текстура, не изменяющаяся со временем
 */
class StaticTexture(private val texture: Texture) : AnimatedTexture() {

    override fun getTexture(timePassed: Long): Texture {
        return texture
    }
}
