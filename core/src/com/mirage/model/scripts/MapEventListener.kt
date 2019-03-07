package com.mirage.model.scripts

import com.badlogic.gdx.maps.MapObject
import com.mirage.model.datastructures.Point
import com.mirage.model.extensions.getString
import com.mirage.model.extensions.tableOf
import com.mirage.model.scripts.ScriptUtils.runScript

class MapEventListener : EventListener{

    override fun onObjectMove(obj: MapObject, oldPos: Point, newPos: Point) {
        if (obj.properties.containsKey("on-move")) {
            val table = tableOf("object" to obj, "oldPos" to oldPos, "newPos" to newPos)
            runScript(obj.properties.getString("on-move"), table)
        }
        if (obj.properties.containsKey("on-tile-entered") &&
                (oldPos.x.toInt() != newPos.x.toInt() || oldPos.y.toInt() != newPos.y.toInt())) {
            val table = tableOf("object" to obj, "oldPos" to oldPos, "newPos" to newPos)
            runScript(obj.properties.getString("on-tile-entered"), table)
        }
    }
}