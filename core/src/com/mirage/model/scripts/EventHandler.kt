package com.mirage.model.scripts

import com.badlogic.gdx.maps.MapObject
import com.mirage.model.datastructures.Point

object EventHandler {

    val listeners = ArrayList<EventListener>()

    fun handleObjectMove(obj: MapObject, oldPos: Point, newPos: Point) {
        for (listener in listeners) {
            listener.onObjectMove(obj, oldPos, newPos)
        }
    }

}