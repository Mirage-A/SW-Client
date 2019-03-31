package com.mirage.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.google.gson.Gson
import com.mirage.utils.maps.GameMap
import com.mirage.utils.maps.GameObject
import com.mirage.utils.maps.GameObjects
import java.io.File
import java.io.InputStream
import java.io.Reader

object Assets {

    val assetsPath = if (DEBUG_MODE && File("./android/assets/").exists())
                                "./android/assets/" else ""

    fun loadFile(path: String) : FileHandle? {
        val file = Gdx.files.internal(assetsPath + path)
        return if (file == null) {
            Log.e("File not found: ${assetsPath + path}")
            null
        }
        else file
    }

    fun loadReader(path: String) : Reader? =
            Gdx.files.internal(assetsPath + path).reader()

    fun loadClientScript(name: String) : Reader? =
            loadReader("scripts/client/$name.lua")

    fun loadLogicScript(name: String) : Reader? =
            loadReader("scripts/logic/$name.lua")

    private val MIN_FILTER = Texture.TextureFilter.MipMapLinearNearest
    private val MAG_FILTER = Texture.TextureFilter.MipMapLinearNearest
    /**
     * Загружает текстуры из папки drawable по относительному пути path и применяет к ним заданные фильтры.
     */
    fun getRawTexture(name: String) : Texture {
        val file = Gdx.files.internal("${assetsPath}drawable/$name.png")
        file ?: return Texture(0, 0, Pixmap.Format.Alpha)
        val loadedTexture = Texture(file, true)
        loadedTexture.setFilter(MIN_FILTER, MAG_FILTER)
        return loadedTexture
    }

    fun loadAnimation(name: String) : InputStream? =
        loadFile("animations/$name.swa")?.read()

    /**
     * Загружает сцену (пару из карты и объектов) по названию карты (папки, в которой хранится карта - например, "test")
     */
    fun loadScene(name: String) : Pair<GameMap, GameObjects> =
            Assets.loadScene(Gdx.files.internal("${assetsPath}maps/$name/map.json").reader(),
                    Gdx.files.internal("${assetsPath}maps/$name/objects.json").reader())

    /**
     * Загружает сцену (пару из карты и объектов)
     */
    fun loadScene(mapReader: Reader, objectsReader: Reader) : Pair<GameMap, GameObjects> {
        val gson = Gson()
        return Pair(gson.fromJson<GameMap>(mapReader, GameMap::class.java),
                gson.fromJson<GameObjects>(mapReader, GameObjects::class.java))
    }

    /**
     * Загружает шаблон объекта по названию
     */
    fun loadTemplate(name: String) : GameObject =
            loadTemplate(Gdx.files.internal("${assetsPath}templates/$name.json").reader())

    /**
     * Загружает шаблон объекта
     */
    fun loadTemplate(reader: Reader) : GameObject =
            Gson().fromJson<GameObject>(reader, GameObject::class.java)
    }
}