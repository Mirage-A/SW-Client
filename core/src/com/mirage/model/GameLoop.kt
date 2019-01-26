package com.mirage.model

import com.mirage.model.scene.Scene
import com.mirage.model.scene.objects.entities.Entity
import com.mirage.model.scene.objects.entities.Player

class GameLoop : Runnable {
    val scene = Scene()

    /**
     * Этот параметр позволяет приостанавливать логику игры, а затем снова запускать
     */
    var isPaused = true

    /**
     * Бесконечный цикл игровой логики
     */
    override fun run() {
        var lastTickTime = System.currentTimeMillis()
        while (true) {
            val deltaTime = System.currentTimeMillis() - lastTickTime
            lastTickTime += deltaTime
            if (!isPaused) {
                for (sceneObject in scene.objects) {
                    if (sceneObject is Entity) {
                        if (sceneObject.isMoving) {
                            sceneObject.position.move(sceneObject.moveAngle, sceneObject.speed * deltaTime / 1000f)
                        }
                    }
                }
            }
        }
    }

}