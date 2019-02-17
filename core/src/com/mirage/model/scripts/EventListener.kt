package com.mirage.model.scripts

import com.badlogic.gdx.maps.MapObject
import com.mirage.model.datastructures.Point

interface EventListener {
    fun onObjectMove(obj: MapObject, oldPos: Point, newPos: Point)
}