package com.mirage.utils.virtualscreen

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.mirage.utils.Assets
import com.mirage.utils.Log
import com.mirage.utils.TILE_HEIGHT
import com.mirage.utils.TILE_WIDTH
import kotlin.math.roundToInt


class VirtualScreenGdxImpl(initialVirtualWidth: Float = 0f, initialVirtualHeight: Float = 0f) : VirtualScreen {

    private val batch: SpriteBatch = SpriteBatch()
    private val camera : OrthographicCamera = OrthographicCamera()

    private val texturesCache: MutableMap<String, Texture> = HashMap()
    private var tileTexturesList: List<TextureRegion> = ArrayList()

    override var width: Float = initialVirtualWidth
    override var height: Float = initialVirtualHeight

    init {
        camera.setToOrtho(false)
        camera.position.x = 0f
        camera.position.y = 0f
        camera.viewportWidth = width
        camera.viewportHeight = height
        camera.update()
        batch.projectionMatrix = camera.combined
    }

    override fun resize(newRealWidth: Int, newRealHeight: Int) {
        val newSize = calculateViewportSize(newRealWidth.toFloat(), newRealHeight.toFloat())
        width = newSize.width
        height = newSize.height
        camera.viewportWidth = width
        camera.viewportHeight = height
        camera.update()
        batch.projectionMatrix = camera.combined
    }

    private val minFilter = Texture.TextureFilter.Nearest
    private val magFilter = Texture.TextureFilter.Nearest

    private val emptyTexture: Texture = Texture(Pixmap(1, 1, Pixmap.Format.Alpha))

    /**
     * Загружает текстуру с заданным названием из файла.
     * Для получения текстуры для отрисовки следует использовать [getTexture]
     */
    private fun loadTexture(name: String): Texture {
        try {
            val file = Assets.loadFile("drawable/$name.png")
            file ?: return emptyTexture
            val loadedTexture = Texture(file, false)
            loadedTexture.setFilter(minFilter, magFilter)
            return loadedTexture
        } catch (ex: Exception) {
            Log.e("Error while loading texture: $name")
            Log.e(ex.message)
            return emptyTexture
        }
    }

    /**
     * Позволяет получить текстуру по названию из кэша.
     * Если текстуры нет в кэше, она загружается и кэшируется.
     */
    private fun getTexture(name: String): Texture {
        val cached = texturesCache[name]
        if (cached != null) return cached
        val loaded = loadTexture(name)
        texturesCache[name] = loaded
        return loaded
    }

    override fun loadAllTextures(textureNames: Iterable<String>) {
        for (textureName in textureNames) {
            texturesCache[textureName] = loadTexture(textureName)
        }
    }

    override fun clearCache() {
        for (texture in texturesCache.values) {
            texture.dispose()
        }
        texturesCache.clear()
    }

    override fun setTileSet(tileSetName: String) {
        tileTexturesList = try {
            val fullTexture = Texture(Assets.loadFile("drawable/tiles/$tileSetName.png"))
            val regions = TextureRegion.split(fullTexture, TILE_WIDTH.roundToInt(), TILE_HEIGHT.roundToInt())
            val res = regions.flatten()
            res.forEach { it.texture.setFilter(minFilter, magFilter) }
            res
        } catch (ex: Exception) {
            Log.e("Error while loading tile set: $tileSetName")
            Log.e(ex.message)
            ArrayList()
        }
    }

    override fun drawTile(tileID: Int, x: Float, y: Float) {
        if (tileID in tileTexturesList.indices) {
            batch.draw(tileTexturesList[tileID], x - TILE_WIDTH / 2, y - TILE_HEIGHT / 2)
        } else {
            Log.e("Error: tileID is out of bounds. tileID=$tileID tileSetSize=${tileTexturesList.size}")
        }
    }

    override fun draw(textureName: String, x: Float, y: Float) {
        val texture = getTexture(textureName)
        batch.draw(texture, x - texture.width / 2, y - texture.height / 2)
    }

    override fun draw(textureName: String, x: Float, y: Float, basicWidth: Float, basicHeight: Float, scale: Float, scaleX: Float, scaleY: Float, angle: Float) {
        val texture = getTexture(textureName)
        //TODO Возможно, вместо basicWidth следует использовать texture.width
        batch.draw(texture,
                x - texture.width / 2f,
                y - texture.height / 2f,
                texture.width / 2f,
                texture.height / 2f,
                basicWidth,
                basicHeight,
                scale * scaleX,
                scale * scaleY,
                Math.toDegrees(angle.toDouble()).toFloat(),
                0, 0,
                basicWidth.toInt(),
                basicHeight.toInt(),
                false, false)
    }

    override fun draw(textureName: String, x: Float, y: Float, originX: Float, originY: Float, width: Float, height: Float, scaleX: Float, scaleY: Float, rotation: Float, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int, flipX: Boolean, flipY: Boolean) {
        val texture = getTexture(textureName)
        batch.draw(texture, x, y, originX, originY, width, height, scaleX, scaleY, rotation, srcX, srcY, srcWidth, srcHeight, flipX, flipY)
    }

    override fun begin() = batch.begin()

    override fun end() = batch.end()
}