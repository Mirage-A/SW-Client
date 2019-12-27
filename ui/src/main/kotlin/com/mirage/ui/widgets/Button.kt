package com.mirage.ui.widgets

import com.mirage.core.datastructures.Point
import com.mirage.core.datastructures.Rectangle
import com.mirage.core.virtualscreen.VirtualScreen

/** Simple button, maybe bordered or with bounded label with some text.
 *  Invokes [onPressed] when this button is touched or when key with [keyCode] is pressed.
 *  */
internal class Button(
        override var textureName: String = "ui/main-menu-btn",
        override var highlightedTextureName: String = textureName,
        override var boundedLabel: LabelWidget? = null,
        sizeUpdater: SizeUpdater? = null,
        var onPressed: () -> Unit = {},
        var keyCode: Int? = null,
        var borderSize: Float = 0f,
        var borderTextureName: String = "ui/btn-border",
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
            isVisible && rect.contains(virtualPoint)

    override fun touchDown(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        return if (rect.contains(virtualPoint)) {
            onPressed()
            true
        }
        else false
    }

    override fun mouseMoved(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        isHighlighted = rect.contains(virtualPoint)
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