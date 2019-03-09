package com.mirage.scriptrunner

import com.badlogic.gdx.maps.MapObject
import com.mirage.gamelogic.datastructures.Point
import com.mirage.gamelogic.extensions.getString
import com.mirage.gamelogic.extensions.tableOf
import com.mirage.scriptrunner.ScriptUtils.runScript

class MapEventListener : EventListener {

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