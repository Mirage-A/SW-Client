package com.mirage.model.scripts

import com.badlogic.gdx.maps.MapObject
import com.mirage.model.datastructures.Point
import com.mirage.model.extensions.getString

class MapEventListener : EventListener{

    override fun onObjectMove(obj: MapObject, oldPos: Point, newPos: Point) {
        if (obj.properties.containsKey("on-move")) {
            val map = mapOf("object" to obj, "oldPos" to oldPos, "newPos" to newPos)
            runScript(obj.properties.getString("on-move"), map)
        }
        if (obj.properties.containsKey("on-tile-entered") &&
                (oldPos.x.toInt() != newPos.x.toInt() || oldPos.y.toInt() != newPos.y.toInt())) {
            val map = mapOf("object" to obj, "oldPos" to oldPos, "newPos" to newPos)
            runScript(obj.properties.getString("on-tile-entered"), map)
        }
    }
}