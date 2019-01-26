package com.mirage.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.mirage.controller.Platform
import com.mirage.view.scene.objects.StaticTexture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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
     * Загружает текстуры из папки ASSETS_PATH по относительному пути path и применяет к ним заданные фильтры.
     * Уже загруженные текстуры кэшируются.
     */
    fun getTexture(path: String): StaticTexture {
        if (texturesCache[path] == null) {
            val staticTexture = StaticTexture(emptyTexture)
            texturesCache[path] = staticTexture
            val context = EmptyCoroutineContext
            val scope = CoroutineScope(context)
            scope.launch {
                val loadedTexture = Texture(Gdx.files.internal(Platform.ASSETS_PATH + path), true)
                loadedTexture.setFilter(MIN_FILTER, MAG_FILTER)
            }
        }
        return texturesCache[path]!!
    }

    /**
     * Очищает кэш с загруженными текстурами
     */
    fun cleanCache() {
        texturesCache.clear()
    }
}
