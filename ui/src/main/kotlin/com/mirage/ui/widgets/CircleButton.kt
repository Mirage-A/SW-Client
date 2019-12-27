package com.mirage.ui.widgets

import com.mirage.core.datastructures.Point
import com.mirage.core.datastructures.Rectangle
import com.mirage.core.virtualscreen.VirtualScreen


/** Circle button, maybe bordered or with bounded label with some text.
 *  Position of rectangle received from [sizeUpdater] is used as center, and its width is used as diameter.
 *  Invokes [onPressed] when this button is touched or when key with [keyCode] is pressed.
 *  */
internal class CircleButton(
        override var textureName: String = "ui/circle-background",
        override var highlightedTextureName: String = textureName,
        override var boundedLabel: LabelWidget? = null,
        sizeUpdater: SizeUpdater? = null,
        var onPressed: () -> Unit = {},
        var keyCode: Int? = null,
        var borderSize: Float = 0f,
        var borderTextureName: String = "ui/circle-border",
        override var isVisible: Boolean = true
) : AbstractButton {

    var sizeUpdater: SizeUpdater? = sizeUpdater
        set(value) {
            boundedLabel?.sizeUpdater = value
            field = value
        }

    private var isHighlighted = false
    private var keyPressed = false

    private var rect: Rectangle = Rectangle()

    private val innerRect: Rectangle
        get() = rect.innerRect(borderSize)

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        rect = sizeUpdater?.invoke(virtualWidth, virtualHeight) ?: Rectangle()
        boundedLabel?.resize(virtualWidth, virtualHeight)
    }

    override fun touchUp(virtualPoint: Point): Boolean =
            isVisible && virtualPoint..rect.position < rect.width / 2f

    override fun touchDown(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        return if (virtualPoint..rect.position < rect.width / 2f) {
            onPressed()
            true
        }
        else false
    }

    override fun mouseMoved(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        isHighlighted = virtualPoint..rect.position < rect.width / 2f
        return isHighlighted
    }

    override fun draw(virtualScreen: VirtualScreen) {
        if (!isVisible) return
        if (borderSize != 0f) virtualScreen.draw(borderTextureName, rect)
        val textureName = if (isHighlighted || keyPressed) highlightedTextureName else textureName
        virtualScreen.draw(textureName, innerRect)
        boundedLabel?.draw(virtualScreen)
    }

}