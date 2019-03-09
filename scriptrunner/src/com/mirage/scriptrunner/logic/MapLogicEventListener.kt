package com.mirage.scriptrunner.logic

import com.badlogic.gdx.maps.MapObject
import com.mirage.scriptrunner.runLogicScript
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.getString
import com.mirage.utils.extensions.tableOf

class MapLogicEventListener : LogicEventListener {

    override fun onObjectMove(obj: MapObject, oldPos: Point, newPos: Point, actions: LogicScriptActions) {
        if (obj.properties.containsKey("on-move")) {
            val table = tableOf("object" to obj, "oldPos" to oldPos, "newPos" to newPos)
            runLogicScript(obj.properties.getString("on-move"), table, actions)
        }
        if (obj.properties.containsKey("on-tile-entered") &&
                (oldPos.x.toInt() != newPos.x.toInt() || oldPos.y.toInt() != newPos.y.toInt())) {
            val table = tableOf("object" to obj, "oldPos" to oldPos, "newPos" to newPos)
            runLogicScript(obj.properties.getString("on-tile-entered"), table, actions)
        }
    }
}