package com.mirage.ui.widgets

import com.mirage.utils.datastructures.Point

interface Widget {

    fun resize(virtualWidth: Float, virtualHeight: Float)

    fun touchUp(virtualPoint: Point): Boolean

    fun touchDown(virtualPoint: Point): Boolean

    fun mouseMoved(virtualPoint: Point)

}