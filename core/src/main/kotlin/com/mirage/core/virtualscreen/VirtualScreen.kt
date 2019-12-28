package com.mirage.core.virtualscreen

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.mirage.core.datastructures.Point
import com.mirage.core.datastructures.Rectangle

/**
 * Cross-platform virtual screen with ability to draw textures on it.
 * Must be used at GDX rendering thread.
 * All textures are stored inside implementation of this interface, client code only uses names of textures.
 * Texture with name NAME will be loaded from file assets/drawable/NAME.png.
 * Center of a virtual screen is always at a point (0, 0).
 * X axis is directed to the right, Y axis is directed upwards.
 * It is guaranteed that all textures will keep their width/height ration when drawn on real screen.
 */
interface VirtualScreen {

    /** Virtual screen size */
    val width: Float
    val height: Float
    /** Real screen size */
    val realWidth: Float
    val realHeight: Float

    val stage: Stage

    /** Translates coordinates of a point on real screen to virtual screen */
    fun projectRealPointOnVirtualScreen(realPoint: Point): Point = Point(
            x = (realPoint.x - realWidth / 2) * (width / realWidth),
            y = -(realPoint.y - realHeight / 2) * (height / realHeight)
    )

    fun projectVirtualPointOnRealScreen(virtualPoint: Point): Point = Point(
            x = virtualPoint.x * (realWidth / width) + realWidth / 2,
            y = -virtualPoint.y * (realHeight / height) + realHeight / 2
    )

    /** Start rendering a new frame. This method must be called before any draw */
    fun begin()

    /** Finish rendering a frame */
    fun end()

    /**
     * Updates virtual screen size on change of real screen size.
     * Virtual screen size differs from real screen size, but keeps its width/height ratio.
     */
    fun resize(newRealWidth: Int, newRealHeight: Int)

    fun drawColorOnAllScreen(r: Float, g: Float, b: Float, a: Float)

    /** Caches all textures with names [textureNames] */
    fun loadAllTextures(textureNames: Iterable<String>)

    /** Disposes all cached textures */
    fun clearCache()

    /** Loads a tile set [tileSetName] for further usage */
    fun setTileSet(tileSetName: String)

    /**
     * Draws a tile with id [tileID] from current tile set load by [setTileSet].
     * Center of a tile texture has coordinates ([x], [y]) on virtual screen.
     */
    fun drawTile(tileID: Int, x: Float, y: Float)

    /** Draws text [text] inside rectangle [rect] */
    fun drawText(text: String, rect: Rectangle)

    /**
     * Creates a label to draw text [text] inside rectangle [rect].
     * Use [Label.draw] to render this text on virtual screen.
     */
    fun createLabel(text: String, rect: Rectangle): Label

    fun createLabel(text: String): Label = createLabel(text, Rectangle())
    fun createLabel(text: String, rect: Rectangle, fontCapHeight: Float): Label
    fun createLabel(text: String, fontCapHeight: Float): Label = createLabel(text, Rectangle(), fontCapHeight)

    fun createTextField(hint: String, rect: Rectangle): TextField
    fun createTextField(hint: String): TextField = createTextField(hint, Rectangle())
    fun createTextField(hint: String, rect: Rectangle, fontCapHeight: Float): TextField
    fun createTextField(hint: String, fontCapHeight: Float): TextField = createTextField(hint, Rectangle(), fontCapHeight)


    /** Draws a texture [textureName] with center at point ([x]. [y])*/
    fun draw(textureName: String, x: Float, y: Float)

    /** Draws a texture [textureName] with center ([x], [y]) and size [width] x [height] */
    fun draw(textureName: String, x: Float, y: Float, width: Float, height: Float)

    /** Draws a texture [textureName] inside rectangle [rect] */
    fun draw(textureName: String, rect: Rectangle)

    /**
     * Draws a texture (this method is useful for rendering animation layers)
     * @param textureName Texture name
     * @param x X-coordinate of texture center on virtual screen
     * @param y Y-coordinate of texture center on virtual screen
     * @param basicWidth Width of source image
     * @param basicHeight Height of source image
     * @param scale Scale coefficient of all texture
     * @param scaleX Scale coefficient on X-axis
     * @param scaleY Scale coefficient on Y-axis
     * @param angle Rotation angle of texture
     */
    fun draw(textureName: String, x: Float, y: Float, basicWidth: Float, basicHeight: Float, scale: Float, scaleX: Float, scaleY: Float, angle: Float)

    /**
     * This method delegates to a [SpriteBatch.draw] method.
     * This method is not recommended to be used.
     * ([x], [y]) is not a center of texture here.
     */
    fun draw(textureName: String, x: Float, y: Float, originX: Float, originY: Float, width: Float, height: Float, scaleX: Float, scaleY: Float, rotation: Float, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int, flipX: Boolean, flipY: Boolean)

    /** Text label */
    interface Label {

        var text: String

        var rect: Rectangle

        fun draw()

        fun resizeFont(virtualWidth: Float, virtualHeight: Float)
    }

    interface TextField {

        var hint: String

        var text: String

        var rect: Rectangle

        fun draw()

        fun resizeFont(virtualWidth: Float, virtualHeight: Float)
    }
}