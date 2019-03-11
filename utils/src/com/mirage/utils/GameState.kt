package com.mirage.utils

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.mirage.utils.MoveDirection
import com.mirage.utils.extensions.isMoving
import com.mirage.utils.extensions.moveDirection
import com.mirage.utils.extensions.get
import java.util.*

class GameState {
    var map = TiledMap()
    val objects = TreeMap<Long, MapObject>()
    var playerID: Long? = null

    fun isPlayerMoving() : Boolean = objects[playerID]?.isMoving ?: false

    fun getPlayerMoveDirection() : MoveDirection = objects[playerID]?.moveDirection ?: MoveDirection.DOWN
}