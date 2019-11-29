package com.mirage.utils.virtualscreen

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.mirage.utils.*
import com.mirage.utils.datastructures.Rectangle
import kotlin.math.roundToInt


open class VirtualScreenGdxImpl(initialVirtualWidth: Float = 0f,
                           initialVirtualHeight: Float = 0f,
                           initialRealWidth: Float = 0f,
                           initialRealHeight: Float = 0f) : VirtualScreen {

    override var width: Float = initialVirtualWidth
    override var height: Float = initialVirtualHeight
    override var realWidth: Float = initialRealWidth
    override var realHeight: Float = initialRealHeight


    private val camera : OrthographicCamera = OrthographicCamera()

    private val batch: SpriteBatch by lazy(LazyThreadSafetyMode.NONE) {
        SpriteBatch().apply {
            camera.apply {
                setToOrtho(false)
                position.x = 0f
                position.y = 0f
                viewportWidth = width
                viewportHeight = height
                update()
            }
            projectionMatrix = camera.combined
        }
    }

    private val texturesCache: MutableMap<String, Texture> = HashMap()
    private var tileTexturesList: List<TextureRegion> = ArrayList()

    override fun resize(newRealWidth: Int, newRealHeight: Int) {
        val newSize = calculateViewportSize(newRealWidth.toFloat(), newRealHeight.toFloat())
        width = newSize.width
        height = newSize.height
        realWidth = newRealWidth.toFloat()
        realHeight = newRealHeight.toFloat()
        camera.viewportWidth = width
        camera.viewportHeight = height
        camera.update()
        batch.projectionMatrix = camera.combined
    }

    private val minFilter = Texture.TextureFilter.Nearest
    private val magFilter = Texture.TextureFilter.Nearest

    private val emptyTexture: Lazy<Texture> = lazy(LazyThreadSafetyMode.NONE) {
        Texture(Pixmap(1, 1, Pixmap.Format.Alpha))
    }

    /**
     * Загружает текстуру с заданным названием из файла.
     * Для получения текстуры для отрисовки следует использовать [getTexture]
     */
    private fun loadTexture(name: String): Texture {
        try {
            val file = Assets.loadFile("drawable/$name.png")
            file ?: return emptyTexture.value
            val loadedTexture = Texture(file, false)
            loadedTexture.setFilter(minFilter, magFilter)
            return loadedTexture
        } catch (ex: Exception) {
            Log.e("Error while loading texture: $name")
            Log.e(ex.message)
            return emptyTexture.value
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

    override fun drawText(text: String, rect: Rectangle) {
        //TODO
        val label = Label(text, Label.LabelStyle(BitmapFont(), Color.BLACK))
        label.isVisible = true
        label.width = rect.width
        label.height = rect.height
        label.setWrap(true)
        label.setX(rect.x, Align.center)
        label.setY(rect.y, Align.center)
        label.setAlignment(Align.center, Align.center)
        label.draw(batch, 255f)
    }

    override fun draw(textureName: String, x: Float, y: Float) {
        val texture = getTexture(textureName)
        batch.draw(texture, x - texture.width / 2f, y - texture.height / 2f)
    }

    override fun draw(textureName: String, x: Float, y: Float, width: Float, height: Float) {
        val texture = getTexture(textureName)
        batch.draw(texture, x - width / 2f,y - height / 2f, width, height)
    }

    override fun draw(textureName: String, rect: Rectangle) =
            draw(textureName, rect.x, rect.y, rect.width, rect.height)

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

    override fun createLabel(text: String, rect: Rectangle): VirtualScreen.Label = GdxLabel(text, rect)

    override fun createLabel(text: String, rect: Rectangle, fontCapHeight: Float) = GdxLabel(text, rect, fontCapHeight)

    inner class GdxLabel internal constructor(
            text: String,
            rect: Rectangle,
            fontCapHeight: Float = 15f
    ) : VirtualScreen.Label {

        override var text: String = text
            set(value) {
                label.setText(value)
                field = value
            }

        private val font = BitmapFont().apply {
            data.setScale(fontCapHeight / data.capHeight)
        }

        private val label = Label(text, Label.LabelStyle(font, Color.BLACK)).apply {
            isVisible = true
            setWrap(true)
            setPosition(rect.x, rect.y, Align.center)
            setSize(rect.width, rect.height)
            setAlignment(Align.center, Align.center)

        }

        override var rect: Rectangle = rect
            set(value) {
                label.apply {
                    setPosition(value.x, value.y, Align.center)
                    setSize(value.width, value.height)
                    setAlignment(Align.center, Align.center)
                }
                field = value
            }


        override fun draw() = label.draw(batch, 255f)

    }
}