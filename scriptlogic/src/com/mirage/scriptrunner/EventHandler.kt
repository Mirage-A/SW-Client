package com.mirage.scriptrunner

import com.badlogic.gdx.maps.MapObject
import com.mirage.gamelogic.datastructures.Point

object EventHandler {

    val listeners = ArrayList<EventListener>()

    fun handleObjectMove(obj: MapObject, oldPos: Point, newPos: Point) {
        for (listener in listeners) {
            listener.onObjectMove(obj, oldPos, newPos)
        }
    }

}