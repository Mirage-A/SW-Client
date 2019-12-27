package com.mirage.ui.widgets

import com.mirage.core.datastructures.Point
import com.mirage.core.virtualscreen.VirtualScreen

/** Shadows the screen with black color with alpha [alpha]. Alpha should in range 0 to 1 */
internal class ShadowingWidget(
        var alpha: Float = 0f,
        override var isVisible: Boolean = false,
        var blocksInput: Boolean = false
) : Widget {

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) virtualScreen.drawColorOnAllScreen(0f, 0f, 0f, alpha)
    }

    override fun resize(virtualWidth: Float, virtualHeight: Float) {}

    override fun touchUp(virtualPoint: Point): Boolean = blocksInput

    override fun touchDown(virtualPoint: Point): Boolean = blocksInput

    override fun mouseMoved(virtualPoint: Point): Boolean = blocksInput

    override fun keyTyped(character: Char): Boolean = blocksInput

    override fun scrolled(amount: Int): Boolean = blocksInput

    override fun touchDragged(virtualPoint: Point): Boolean = blocksInput

    override fun keyUp(keycode: Int): Boolean = blocksInput

    override fun keyDown(keycode: Int): Boolean = blocksInput

}