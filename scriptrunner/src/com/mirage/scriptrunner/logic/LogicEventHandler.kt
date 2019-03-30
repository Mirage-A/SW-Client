package com.mirage.scriptrunner.logic

import com.badlogic.gdx.maps.MapObject
import com.mirage.utils.datastructures.MutablePoint

class LogicEventHandler(private val actions: LogicScriptActions) {

    val listeners = ArrayList<LogicEventListener>()

    fun handleObjectMove(obj: MapObject, oldPos: MutablePoint, newPos: MutablePoint) {
        for (listener in listeners) {
            listener.onObjectMove(obj, oldPos, newPos, actions)
        }
    }

}