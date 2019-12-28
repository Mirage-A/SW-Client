package com.mirage.ui.widgets

import com.mirage.core.utils.Point
import com.mirage.core.utils.Rectangle
import com.mirage.core.virtualscreen.VirtualScreen


/** Circle button, maybe bordered or with bounded label with some text.
 *  Position of rectangle received from [sizeUpdater] is used as center, and its width is used as diameter.
 *  Invokes [onPressed] when this button is touched or when key with [keyCode] is pressed.
 *  */
internal class CircleButton(
        override var textureName: String = "ui/circle-background",
        override var highlightedTextureName: String = textureName,
        override var pressedTextureName: String = highlightedTextureName,
        override var boundedLabel: LabelWidget? = null,
        sizeUpdater: SizeUpdater? = null,
        var onPressed: () -> Unit = {},
        var keyCode: Int? = null,
        var borderSize: Float = 0f,
        var borderTextureName: String = "ui/circle-border",
        override var isVisible: Boolean = true
) : AbstractButton {

    init {
        boundedLabel?.sizeUpdater = sizeUpdater
    }

    var sizeUpdater: SizeUpdater? = sizeUpdater
        set(value) {
            boundedLabel?.sizeUpdater = value
            field = value
        }

    private var isHighlighted = false
    private var isPressed = false
    private var keyPressed = false

    private var rect: Rectangle = Rectangle()

    private val innerRect: Rectangle
        get() = rect.innerRect(borderSize)

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        rect = sizeUpdater?.invoke(virtualWidth, virtualHeight) ?: Rectangle()
        boundedLabel?.resize(virtualWidth, virtualHeight)
    }

    override fun touchUp(virtualPoint: Point): Boolean {
        if (!isVisible || !isPressed) return false
        isPressed = false
        return if (virtualPoint..rect.position < rect.width / 2f) {
            onPressed()
            true
        } else false
    }

    override fun touchDown(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        isPressed = virtualPoint..rect.position < rect.width / 2f
        return isPressed
    }

    override fun keyUp(keycode: Int): Boolean {
        return if (isVisible && keycode == keyCode) {
            keyPressed = false
            onPressed()
            true
        } else false
    }


    override fun keyDown(keycode: Int): Boolean {
        return if (isVisible && keycode == keyCode) {
            keyPressed = true
            true
        } else false
    }


    override fun mouseMoved(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        isHighlighted = virtualPoint..rect.position < rect.width / 2f
        return isHighlighted
    }

    override fun touchDragged(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        isHighlighted = virtualPoint..rect.position < rect.width / 2f
        return isHighlighted
    }

    override fun draw(virtualScreen: VirtualScreen) {
        if (!isVisible) return
        if (borderSize != 0f) virtualScreen.draw(borderTextureName, rect)
        val textureName = when {
            isPressed || keyPressed -> pressedTextureName
            isHighlighted -> highlightedTextureName
            else -> textureName
        }
        virtualScreen.draw(textureName, innerRect)
        boundedLabel?.draw(virtualScreen)
    }

}