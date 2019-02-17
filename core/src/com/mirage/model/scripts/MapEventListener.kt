package com.mirage.model.scripts

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.MapProperties
import com.mirage.model.datastructures.Point
import com.mirage.model.extensions.getString

class MapEventListener : EventListener{

    override fun onObjectMove(obj: MapObject, oldPos: Point, newPos: Point) {
        if (obj.properties.containsKey("on-move")) {
            val props = MapProperties()
            props.put("object", obj)
            props.put("oldPos", oldPos)
            props.put("newPos", newPos)
            ScriptLoader.load(obj.properties.getString("on-move")).run(props)
        }
        if (obj.properties.containsKey("on-tile-entered") &&
                (oldPos.x.toInt() != newPos.x.toInt() || oldPos.y.toInt() != newPos.y.toInt())) {
            val props = MapProperties()
            props.put("object", obj)
            props.put("oldPos", oldPos)
            props.put("newPos", newPos)
            ScriptLoader.load(obj.properties.getString("on-tile-entered")).run(props)
        }
    }
}