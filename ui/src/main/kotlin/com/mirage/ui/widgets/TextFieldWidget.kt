package com.mirage.ui.widgets

import com.mirage.core.datastructures.Rectangle
import com.mirage.core.virtualscreen.VirtualScreen

/** Widget wrapper for LibGDX TextField */
internal class TextFieldWidget(
        virtualScreen: VirtualScreen,
        hint: String = "",
        fontCapHeight: Float = 16f,
        var sizeUpdater: SizeUpdater? = null,
        override var isVisible: Boolean = true
) : Widget {

    private val textField = virtualScreen.createTextField(hint, fontCapHeight)

    /** Hint is displayed when no text is entered */
    var hint: String
        get() = textField.hint
        set(value) { textField.hint = value }

    /** Entered text */
    var text: String
        get() = textField.text
        set(value) { textField.text = value }

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        textField.rect = sizeUpdater?.invoke(virtualWidth, virtualHeight) ?: Rectangle()
        textField.resizeFont(virtualWidth, virtualHeight)
    }

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) textField.draw()
    }
}