package com.mirage.scriptrunner.logic

import com.badlogic.gdx.maps.MapObject
import com.mirage.utils.datastructures.Point

class LogicEventHandler(private val actions: LogicScriptActions) {

    val listeners = ArrayList<LogicEventListener>()

    fun handleObjectMove(obj: MapObject, oldPos: Point, newPos: Point) {
        for (listener in listeners) {
            listener.onObjectMove(obj, oldPos, newPos, actions)
        }
    }

}