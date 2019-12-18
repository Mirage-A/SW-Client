package com.mirage.ui.widgets

import com.mirage.utils.datastructures.Point
import com.mirage.utils.virtualscreen.VirtualScreen

interface Widget {

    var isVisible: Boolean

    fun resize(virtualWidth: Float, virtualHeight: Float)

    fun touchUp(virtualPoint: Point): Boolean

    fun touchDown(virtualPoint: Point): Boolean

    fun mouseMoved(virtualPoint: Point)

    fun draw(virtualScreen: VirtualScreen)

    fun unpress() {}

}