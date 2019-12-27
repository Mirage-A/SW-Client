package com.mirage.ui.screens

import com.mirage.core.datastructures.Point
import com.mirage.core.messaging.ClientMessage
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.ui.widgets.Widget

typealias ClientMessageListener = (ClientMessage) -> Unit

abstract class AbstractScreen(protected val virtualScreen: VirtualScreen) : Screen {

    init {
        virtualScreen.stage.clear()
    }

    internal abstract val rootWidget: Widget

    override fun keyDown(keycode: Int): Boolean = rootWidget.keyDown(keycode)

    override fun keyUp(keycode: Int): Boolean = rootWidget.keyUp(keycode)

    override fun keyTyped(character: Char): Boolean = rootWidget.keyTyped(character)

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean =
            rootWidget.touchDown(getVirtualPoint(screenX, screenY), pointer, button)

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean =
            rootWidget.touchUp(getVirtualPoint(screenX, screenY), pointer, button)

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean =
            rootWidget.touchDragged(getVirtualPoint(screenX, screenY), pointer)

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean =
            rootWidget.mouseMoved(getVirtualPoint(screenX, screenY))

    override fun scrolled(amount: Int): Boolean = rootWidget.scrolled(amount)

    override fun resize(virtualWidth: Float, virtualHeight: Float) = rootWidget.resize(virtualWidth, virtualHeight)

    override fun render(virtualScreen: VirtualScreen) = rootWidget.draw(virtualScreen)

    private fun getVirtualPoint(screenX: Int, screenY: Int) =
            virtualScreen.projectRealPointOnVirtualScreen(Point(screenX.toFloat(), screenY.toFloat()))

}