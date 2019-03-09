package com.mirage.scriptrunner

import com.badlogic.gdx.maps.MapObject
import com.mirage.gamelogic.datastructures.Point

interface EventListener {
    fun onObjectMove(obj: MapObject, oldPos: Point, newPos: Point)
}