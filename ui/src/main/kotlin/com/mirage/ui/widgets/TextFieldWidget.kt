package com.mirage.ui.widgets

import com.mirage.core.utils.Point
import com.mirage.core.utils.Rectangle
import com.mirage.core.virtualscreen.VirtualScreen

/** Widget wrapper for LibGDX TextField. Only one TextFieldWidget can be active at a time */
internal class TextFieldWidget(
        private val virtualScreen: VirtualScreen,
        hint: String = "",
        fontCapHeight: Float = 16f,
        var sizeUpdater: SizeUpdater? = null,
        override var isVisible: Boolean = true
) : Widget {

    init {
        virtualScreen.stage.clear()
    }

    private val textField = virtualScreen.createTextField(hint, fontCapHeight)

    /** Hint is displayed when no text is entered */
    var hint: String
        get() = textField.hint
        set(value) {
            textField.hint = value
        }

    /** Entered text */
    var text: String
        get() = textField.text
        set(value) {
            textField.text = value
        }

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        textField.rect = sizeUpdater?.invoke(virtualWidth, virtualHeight) ?: Rectangle()
        textField.resizeFont(virtualWidth, virtualHeight)
    }

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) textField.draw()
    }

    override fun touchUp(virtualPoint: Point, pointer: Int, button: Int): Boolean {
        println("$virtualPoint ${textField.rect}")
        if (isVisible && textField.rect.contains(virtualPoint)) {
            val realPoint = virtualScreen.projectVirtualPointOnRealScreen(virtualPoint)
            println(realPoint)
            return virtualScreen.stage.touchUp(realPoint.x.toInt(), realPoint.y.toInt(), pointer, button)
        }
        return false
    }

    override fun touchDown(virtualPoint: Point, pointer: Int, button: Int): Boolean {
        if (isVisible && textField.rect.contains(virtualPoint)) {
            val realPoint = virtualScreen.projectVirtualPointOnRealScreen(virtualPoint)
            return virtualScreen.stage.touchDown(realPoint.x.toInt(), realPoint.y.toInt(), pointer, button)
        }
        return false
    }

    override fun mouseMoved(virtualPoint: Point): Boolean {
        if (isVisible) {
            val realPoint = virtualScreen.projectVirtualPointOnRealScreen(virtualPoint)
            return virtualScreen.stage.mouseMoved(realPoint.x.toInt(), realPoint.y.toInt())
        }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        if (isVisible) {
            return virtualScreen.stage.keyTyped(character)
        }
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        if (isVisible) {
            return virtualScreen.stage.scrolled(amount)
        }
        return false
    }

    override fun touchDragged(virtualPoint: Point, pointer: Int): Boolean {
        if (isVisible) {
            val realPoint = virtualScreen.projectVirtualPointOnRealScreen(virtualPoint)
            return virtualScreen.stage.touchDragged(realPoint.x.toInt(), realPoint.y.toInt(), pointer)
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (isVisible) {
            return virtualScreen.stage.keyUp(keycode)
        }
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        if (isVisible) {
            return virtualScreen.stage.keyDown(keycode)
        }
        return false
    }

}