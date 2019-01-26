package com.mirage.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.mirage.controller.Platform
import com.mirage.view.gameobjects.StaticTexture
import java.util.*

object TextureLoader {
    private val MIN_FILTER = Texture.TextureFilter.MipMapLinearNearest
    private val MAG_FILTER = Texture.TextureFilter.MipMapLinearNearest

    /**
     * Словарь с загруженными текстурами
     */
    private val texturesCache : MutableMap<String, StaticTexture> = TreeMap()

    /**
     * Пустая текстура, которой заменяется ещё загружаемая текстура
     */
    private val emptyTexture = Texture(0, 0, Pixmap.Format.Alpha)

    /**
     * Создаёт объект StaticTexture (наследник Image), хранящий текстуру, загружаемую по данному пути через getRawTexture
     * Созданные объекты кэшируются.
     */
    fun getStaticTexture(path: String): StaticTexture {
        if (texturesCache[path] == null) {
            texturesCache[path] = StaticTexture(getRawTexture(path))
        }
        return texturesCache[path]!!
    }

    /**
     * Загружает текстуры из папки ASSETS_PATH по относительному пути path и применяет к ним заданные фильтры.
     * Текстуры не кэшируются.
     */
    fun getRawTexture(path: String) : Texture {
        val loadedTexture = Texture(Gdx.files.internal(Platform.ASSETS_PATH + path), true)
        loadedTexture.setFilter(MIN_FILTER, MAG_FILTER)
        return loadedTexture
    }

    /**
     * Очищает кэш с загруженными текстурами
     */
    fun cleanCache() {
        texturesCache.clear()
    }
}
