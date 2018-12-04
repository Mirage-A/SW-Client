package com.mirage.view.scene.objects.humanoid

import com.badlogic.gdx.graphics.Texture

/**
 * Текстура, которая изменяется со временем, используется для анимации брони/оружия гуманоидов
 * Можно использовать наследника StaticTexture для обычной статичной текстуры
 */
abstract class AnimatedTexture {

    abstract fun getTexture(timePassed: Long): Texture
}
