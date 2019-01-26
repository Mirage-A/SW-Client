package com.mirage.model.scene

import com.mirage.model.scene.objects.entities.Player

object MazeGenerator {
    fun generateMaze(width: Int, height: Int) : Scene {
        val scene = Scene()
        scene.width = width
        scene.height = height
        scene.player = Player(Point(1.5f, 1.5f))
        scene.objects.add(scene.player)
        scene.tileMatrix = Array(width) {IntArray(height) {0} }
        scene.passabilityMatrix = Array(width) { Array(height) { PassabilityType.ALL_FREE } }
        for (i in 0 until width) {
            scene.passabilityMatrix[i][0] = PassabilityType.BLOCKED
            scene.passabilityMatrix[i][height - 1] = PassabilityType.BLOCKED
        }
        for (j in 0 until height) {
            scene.passabilityMatrix[0][j] = PassabilityType.BLOCKED
            scene.passabilityMatrix[width - 1][j] = PassabilityType.BLOCKED
        }
        return scene
    }
}