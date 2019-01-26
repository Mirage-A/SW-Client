package com.mirage.view.scene.objects

import com.badlogic.gdx.graphics.Texture

/**
 * Обёртка для текстуры
 * AnimatedTexture - текстура, которая изменяется со временем, используется для анимации брони/оружия гуманоидов
 * Можно использовать наследника StaticTexture для обычной статичной текстуры
 */
abstract class Image {

    abstract fun getTexture(): Texture
}
