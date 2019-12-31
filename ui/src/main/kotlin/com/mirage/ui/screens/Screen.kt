package com.mirage.ui.screens

import com.mirage.core.utils.Point
import com.mirage.core.messaging.ClientMessage
import com.mirage.core.VirtualScreen
import com.mirage.core.messaging.ServerMessage
import com.mirage.ui.widgets.Widget

typealias ClientMessageListener = (ClientMessage) -> Unit

abstract class Screen(
        protected val virtualScreen: VirtualScreen,
        protected val listener: ClientMessageListener
) {

    internal abstract val rootWidget: Widget

    open fun keyDown(keycode: Int): Boolean = rootWidget.keyDown(keycode)

    open fun keyUp(keycode: Int): Boolean = rootWidget.keyUp(keycode)

    open fun keyTyped(character: Char): Boolean = rootWidget.keyTyped(character)

    open fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean =
            rootWidget.touchDown(getVirtualPoint(screenX, screenY), pointer, button)

    open fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean =
            rootWidget.touchUp(getVirtualPoint(screenX, screenY), pointer, button)

    open fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean =
            rootWidget.touchDragged(getVirtualPoint(screenX, screenY), pointer)

    open fun mouseMoved(screenX: Int, screenY: Int): Boolean =
            rootWidget.mouseMoved(getVirtualPoint(screenX, screenY))

    open fun scrolled(amount: Int): Boolean = rootWidget.scrolled(amount)

    open fun handleServerMessage(msg: ServerMessage) {}

    open fun resize(virtualWidth: Float, virtualHeight: Float) = rootWidget.resize(virtualWidth, virtualHeight)

    open fun render() = rootWidget.draw(virtualScreen)

    private fun getVirtualPoint(screenX: Int, screenY: Int) =
            virtualScreen.projectRealPointOnVirtualScreen(Point(screenX.toFloat(), screenY.toFloat()))

}