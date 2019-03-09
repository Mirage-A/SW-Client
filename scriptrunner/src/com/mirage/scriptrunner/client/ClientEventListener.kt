package com.mirage.scriptrunner.client

import com.badlogic.gdx.maps.MapObject
import com.mirage.utils.datastructures.Point

interface ClientEventListener {
    fun onObjectMove(obj: MapObject, oldPos: Point, newPos: Point, actions: ClientScriptActions)
}