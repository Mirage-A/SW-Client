package com.mirage.model.scene

import com.mirage.model.scene.objects.entities.Player

object MazeGenerator {
    fun generateMaze(width: Int, height: Int) : Scene {
        val scene = Scene()
        scene.width = width
        scene.height = height
        scene.player = Player()
        scene.objects.add(scene.player)
        scene.tileMatrix = Array(width) {IntArray(height) {0} }
        return scene
    }
}