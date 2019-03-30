package com.mirage.scriptrunner.logic

import com.badlogic.gdx.maps.MapObject
import com.mirage.utils.datastructures.MutablePoint

interface LogicEventListener {
    fun onObjectMove(obj: MapObject, oldPos: MutablePoint, newPos: MutablePoint, actions: LogicScriptActions)
}