package com.mirage.model.scene

import com.mirage.model.scene.objects.SceneObject
import com.mirage.model.scene.objects.entities.Player
import java.io.File
import java.util.LinkedList

class Scene {
    var width = 0
    var height = 0
    val objects: LinkedList<SceneObject> = LinkedList()
    var tileMatrix: Array<IntArray> = Array(0) {IntArray(0) {0} }
    var passabilityMatrix: Array<Array<PassabilityType>> = Array(0) { Array(0) {PassabilityType.ALL_FREE} }
    var player: Player = Player()
}