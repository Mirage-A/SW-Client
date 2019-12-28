package com.mirage.ui.widgets

import com.mirage.core.datastructures.Point
import com.mirage.core.extensions.IntervalMillis
import com.mirage.core.virtualscreen.VirtualScreen
import kotlin.math.min

/**
 * Shadows the screen with black color, smoothly increasing alpha from 0 to [maxAlpha] in [shadowingTime] ms.
 *  [maxAlpha] should in range 0 to 1
 */
internal class ShadowingWidget(
        var maxAlpha: Float = 1f,
        var shadowingTime: IntervalMillis = 0L,
        isVisible: Boolean = false,
        var blocksInput: Boolean = false
) : Widget {

    private var shadowingStartTime = 0L

    override var isVisible: Boolean = isVisible
        set(value) {
            if (value) shadowingStartTime = System.currentTimeMillis()
            field = value
        }

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) {
            val progress = (System.currentTimeMillis() - shadowingStartTime).toFloat() / shadowingTime.toFloat()
            val alpha = maxAlpha * min(1f, progress)
            virtualScreen.drawColorOnAllScreen(r = 0f, g = 0f, b = 0f, a = alpha)
        }
    }

    override fun resize(virtualWidth: Float, virtualHeight: Float) {}

    override fun touchUp(virtualPoint: Point): Boolean = isVisible && blocksInput

    override fun touchDown(virtualPoint: Point): Boolean = isVisible && blocksInput

    override fun mouseMoved(virtualPoint: Point): Boolean = isVisible && blocksInput

    override fun keyTyped(character: Char): Boolean = isVisible && blocksInput

    override fun scrolled(amount: Int): Boolean = isVisible && blocksInput

    override fun touchDragged(virtualPoint: Point): Boolean = isVisible && blocksInput

    override fun keyUp(keycode: Int): Boolean = isVisible && blocksInput

    override fun keyDown(keycode: Int): Boolean = isVisible && blocksInput

}