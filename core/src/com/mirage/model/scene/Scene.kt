package com.mirage.model.scene

import com.mirage.model.scene.objects.SceneObject
import com.mirage.model.scene.objects.entities.Player
import java.util.*

class Scene {
    var width = 0
    var height = 0
    val objects: LinkedList<SceneObject> = LinkedList()
    var tileMatrix: Array<IntArray> = Array(0) {IntArray(0) {0} }
    var approachabilityMatrix: Array<Array<ApproachabilityType>> = Array(0) { Array(0) {ApproachabilityType.ALL_FREE} }
    var player: Player = Player()
}