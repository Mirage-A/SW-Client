package com.mirage.scriptrunner.client

import com.badlogic.gdx.maps.MapObject
import com.mirage.utils.datastructures.Point

class ClientEventHandler(private val actions: ClientScriptActions) {

    val listeners = ArrayList<ClientEventListener>()

    fun handleObjectMove(obj: MapObject, oldPos: Point, newPos: Point, actions: ClientScriptActions) {
        for (listener in listeners) {
            listener.onObjectMove(obj, oldPos, newPos, actions)
        }
    }

}