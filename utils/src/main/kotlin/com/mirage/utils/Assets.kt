package com.mirage.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.nhaarman.mockitokotlin2.mock
import java.io.File
import java.io.InputStream
import java.io.Reader
import kotlin.math.roundToInt
import kotlin.system.exitProcess

//TODO Сделать так, чтобы в этом объекте кэшировались все текстуры, и чтобы текстуры можно было получать только по названию,
//TODO т.е. ни один модуль не получал объекты класса Texture.
//TODO Тогда надо будет также вызывать batch.draw внутри этого объекта, ну и тогда нужно будет хранить batch здесь же.
//TODO Тогда можно будет сделать обертку над всей графикой из LibGDX, что серьезно облегчит тестирование.
object Assets {

    val assetsPath = if ((PLATFORM == "test" || PLATFORM == "desktop-test") && File("./android/assets/").exists())
        "./android/assets/" else ""

    val emptyTexture: Texture
            get() = Texture(Pixmap(1, 1, Pixmap.Format.Alpha))

    private val assetsResolver : FileHandleResolver = when (PLATFORM) {
        "test" -> FileHandleResolver {
            FileHandle(File(File("").absoluteFile.parentFile.absolutePath + "/android/assets/$it"))
        }
        "desktop-test" -> FileHandleResolver {
            FileHandle(File(File("").absolutePath + "/android/assets/$it"))
        }
        "server" -> FileHandleResolver {
            FileHandle(File(File("").absolutePath + "/android/assets/$it"))
        }
        "desktop", "android", "ios" -> FileHandleResolver {
            Gdx.files.internal(it)
        }
        else -> {
            Log.e("Unknown platform: $PLATFORM")
            FileHandleResolver {
                Gdx.files.internal(it)
            }
        }
    }

    fun loadFile(path: String) : FileHandle? =
        try {
            val file = assetsResolver.resolve(path)
            if (file == null || !file.exists()) {
                Log.e("File not found: $path")
                null
            }
            else file
        }
        catch (ex: Exception) {
            Log.e("File not found: $path")
            null
        }


    fun loadReader(path: String) : Reader? =
            loadFile(path)?.reader()

    fun loadScript(name: String) : Reader? =
            loadReader("scripts/$name.lua")

    private val MIN_FILTER = Texture.TextureFilter.Nearest
    private val MAG_FILTER = Texture.TextureFilter.Nearest
    /**
     * Загружает текстуры из папки drawable по относительному пути path и применяет к ним заданные фильтры.
     */
    fun getRawTexture(name: String) : Texture {
        try {
            val file = loadFile("drawable/$name.png")
            file ?: return emptyTexture
            val loadedTexture = Texture(file, true)
            loadedTexture.setFilter(MIN_FILTER, MAG_FILTER)
            return loadedTexture
        }
        catch (ex: Exception) {
            Log.e("Error while loading texture: $name")
            Log.e(ex.message)
            return emptyTexture
        }
    }

    fun loadAnimation(name: String) : InputStream? =
        loadFile("animations/$name.swa")?.read()

    fun loadTileTexturesList(name: String) : List<TextureRegion> = try {
        val fullTexture = getRawTexture("tiles/$name")
        val regions = TextureRegion.split(fullTexture, TILE_WIDTH.roundToInt(), TILE_HEIGHT.roundToInt())
        val res = regions.flatten()
        res.forEach {it.texture.setFilter(MIN_FILTER, MAG_FILTER)}
        res
    }
    catch (ex: Exception) {
        Log.e("Error while loading tile set: $name")
        Log.e(ex.message)
        ArrayList()
    }


}