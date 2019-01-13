package com.mirage.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.mirage.controller.Platform

object TextureLoader {
    private val MIN_FILTER = Texture.TextureFilter.MipMapLinearNearest
    private val MAG_FILTER = Texture.TextureFilter.MipMapLinearNearest

    /**
     * Загружает текстуры из папки ASSETS_PATH и применяет к ним заданные фильтры
     */
    fun load(path: String): Texture {
        val t = Texture(Gdx.files.internal(Platform.ASSETS_PATH + path), true)
        t.setFilter(MIN_FILTER, MAG_FILTER)
        return t
    }
}
