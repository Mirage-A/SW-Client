package com.mirage.view

import com.mirage.utils.Assets
import com.mirage.view.gameobjects.Image
import com.mirage.view.gameobjects.StaticTexture
import java.util.*

object TextureLoader {

    /**
     * Словарь с загруженными текстурами
     */
    private val texturesCache : MutableMap<String, StaticTexture> = TreeMap()

    /**
     * Создаёт объект StaticTexture (наследник Image), хранящий текстуру, загружаемую по данному пути через getRawTexture
     * Созданные объекты кэшируются.
     */
    fun getStaticTexture(path: String, alignment: Image.Alignment = Image.Alignment.BOTTOM_LEFT): StaticTexture {
        val cachedTexture = texturesCache[path]
        return if (cachedTexture == null) {
            val texture = StaticTexture(Assets.getRawTexture(path), alignment)
            texturesCache[path] = texture
            texture
        }
        else cachedTexture
    }

    /**
     * Очищает кэш с загруженными текстурами
     */
    fun cleanCache() {
        texturesCache.clear()
    }
}
