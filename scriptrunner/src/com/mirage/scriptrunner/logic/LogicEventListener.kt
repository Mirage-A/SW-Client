package com.mirage.scriptrunner.logic

import com.badlogic.gdx.maps.MapObject
import com.mirage.utils.datastructures.Point

interface LogicEventListener {
    fun onObjectMove(obj: MapObject, oldPos: Point, newPos: Point, actions: LogicScriptActions)
}