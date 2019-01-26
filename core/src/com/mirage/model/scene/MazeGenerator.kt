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
        scene.approachabilityMatrix = Array(width) { Array(height) { ApproachabilityType.ALL_FREE } }
        for (i in 0 until width) {
            scene.approachabilityMatrix[i][0] = ApproachabilityType.BLOCKED
            scene.approachabilityMatrix[i][height - 1] = ApproachabilityType.BLOCKED
            scene.tileMatrix[i][0] = 1
            scene.tileMatrix[i][height - 1] = 1
        }
        for (j in 0 until height) {
            scene.approachabilityMatrix[0][j] = ApproachabilityType.BLOCKED
            scene.approachabilityMatrix[width - 1][j] = ApproachabilityType.BLOCKED
            scene.tileMatrix[0][j] = 1
            scene.tileMatrix[width - 1][j] = 1
        }

        scene.tileMatrix[2][2] = 1

        scene.approachabilityMatrix[2][2] = ApproachabilityType.BLOCKED

        return scene
    }
}