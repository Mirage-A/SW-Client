package com.mirage.model.scene

import com.mirage.model.datastructures.IntPair
import com.mirage.model.scene.objects.SceneObject
import com.mirage.model.scene.objects.entities.Player
import java.util.*

class Scene(val width : Int = 0, val height : Int = 0) {
    val objects: LinkedList<SceneObject> = LinkedList()
    private var tileMatrix: Array<IntArray> = Array(width) {IntArray(height) {0} }
    private var approachabilityMatrix: Array<Array<ApproachabilityType>> = Array(width) { Array(height) {ApproachabilityType.ALL_FREE} }
    var player: Player = Player()

    fun getTileId(x: Int, y: Int) : Int = tileMatrix[x][y]

    fun getTileId(p: IntPair) : Int = tileMatrix[p.x][p.y]

    fun setTileId(x: Int, y: Int, id: Int) {
        tileMatrix[x][y] = id
    }

    fun setTileId(p: IntPair, id: Int) {
        tileMatrix[p.x][p.y] = id
    }

    fun getApproachability(x: Int, y: Int) : ApproachabilityType = approachabilityMatrix[x][y]

    fun getApproachability(p: IntPair) : ApproachabilityType = approachabilityMatrix[p.x][p.y]

    fun setApproachability(x: Int, y: Int, approachabilityType: ApproachabilityType) {
        approachabilityMatrix[x][y] = approachabilityType
    }

    fun setApproachability(p: IntPair, approachabilityType: ApproachabilityType) {
        approachabilityMatrix[p.x][p.y] = approachabilityType
    }

    fun isTileWalkable(x: Int, y: Int) : Boolean = approachabilityMatrix[x][y].isWalkable()

    fun isTileWalkable(p: IntPair) : Boolean = approachabilityMatrix[p.x][p.y].isWalkable()

    fun isTileShootable(x: Int, y: Int) : Boolean = approachabilityMatrix[x][y].isShootable()

    fun isTileShootable(p: IntPair) : Boolean = approachabilityMatrix[p.x][p.y].isShootable()
}