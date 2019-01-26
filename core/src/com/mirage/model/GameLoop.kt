package com.mirage.model

import com.mirage.model.scene.Scene
import com.mirage.model.scene.objects.entities.Entity
import com.mirage.model.scene.objects.entities.Player

class GameLoop : Runnable {
    var scene = Scene()

    /**
     * Этот параметр позволяет приостанавливать логику игры, а затем снова запускать
     */
    var isPaused = true

    /**
     * Значение, которое равно true, если на данный момент выполняется итерация цикла
     * Безопасно выйти из цикла можно только когда isLooping == false
     */
    private var isLooping = false

    /**
     * Бесконечный цикл игровой логики
     */
    override fun run() {
        var lastTickTime = System.currentTimeMillis()
        while (true) {
            val deltaTime = System.currentTimeMillis() - lastTickTime
            lastTickTime += deltaTime
            if (!isPaused) {
                isLooping = true
                for (sceneObject in scene.objects) {
                    if (sceneObject is Entity) {
                        if (sceneObject.isMoving) {
                            sceneObject.position.move(sceneObject.moveAngle, sceneObject.speed * deltaTime / 1000f)
                        }
                    }
                }
            }
            else {
                isLooping = false
            }
        }
    }

    /**
     * Безопасно останавливает цикл (ждет, пока завершится последняя итерация)
     */
    fun pauseAndAwait() {
        isPaused = true
        while (isLooping) {}
    }

}