package com.mirage.ui.widgets

import com.mirage.core.datastructures.Rectangle
import com.mirage.core.virtualscreen.VirtualScreen

internal class LabelWidget(
        virtualScreen: VirtualScreen,
        text: String = "",
        fontCapHeight: Float = 16f,
        var sizeUpdater: SizeUpdater? = null
) : Widget {

    private val label = virtualScreen.createLabel(text, fontCapHeight)

    var text: String
        get() = label.text
        set(value) { label.text = value }

    override var isVisible = true

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        label.rect = sizeUpdater?.invoke(virtualWidth, virtualHeight) ?: Rectangle()
        label.resizeFont(virtualWidth, virtualHeight)
    }

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) label.draw()
    }
}