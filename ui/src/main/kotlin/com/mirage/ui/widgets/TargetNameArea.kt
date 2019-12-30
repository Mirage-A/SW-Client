package com.mirage.ui.widgets

import com.mirage.core.utils.Point
import com.mirage.core.utils.Rectangle
import com.mirage.core.VirtualScreen

internal class TargetNameArea(
        var borderTextureName: String = "ui/game/health-border",
        var textAreaTextureName: String = "ui/game/health-lost",
        var boundedLabel: LabelWidget? = null,
        var innerMargin: Float = 0f,
        sizeUpdater: SizeUpdater? = null,
        override var isVisible: Boolean = true
) : Widget {

    var sizeUpdater: SizeUpdater? = sizeUpdater
        set(value) {
            boundedLabel?.sizeUpdater = value
            field = value
        }

    private var rect: Rectangle = Rectangle()

    private val resourceRect: Rectangle
        get() = rect.innerRect(innerMargin)

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        rect = sizeUpdater?.invoke(virtualWidth, virtualHeight) ?: Rectangle()
        boundedLabel?.resize(virtualWidth, virtualHeight)
    }

    override fun draw(virtualScreen: VirtualScreen) {
        if (!isVisible) return
        virtualScreen.draw(borderTextureName, rect)
        virtualScreen.draw(textAreaTextureName, resourceRect)
        boundedLabel?.draw(virtualScreen)

    }

    override fun touchDown(virtualPoint: Point): Boolean = isVisible && rect.contains(virtualPoint)

    override fun touchUp(virtualPoint: Point): Boolean = isVisible && rect.contains(virtualPoint)

}