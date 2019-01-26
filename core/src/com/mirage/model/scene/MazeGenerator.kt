package com.mirage.model.scene

import com.mirage.model.scene.Scene
import com.mirage.model.scene.objects.entities.Player

object MazeGenerator {
    fun generateMaze(width: Int, height: Int) : Scene {
        val scene = Scene()
        scene.width = width
        scene.height = height
        scene.objects.add(Player())
        return scene
    }
}