package com.mirage.model

import com.badlogic.gdx.Gdx
import com.mirage.model.datastructures.Point
import com.mirage.model.scene.Scene
import com.mirage.model.scene.objects.entities.Entity
import com.mirage.model.scripts.ScriptLoader
import com.mirage.view.Log

class GameLoop {
    var scene = Scene()

    /**
     * Этот параметр позволяет приостанавливать логику игры, а затем снова запускать
     */
    var isPaused = true

    /**
     * Перемещение персонажа, которое считается достаточно малым, чтобы при таком перемещении можно было рассматривать только соседние тайлы
     * Длинные перемещения разбиваются на малые такой длины
     */
    private val smallRange = 0.5f

    /**
     * Лимит кол-ва итераций цикла за секунду
     */
    private val ticksPerSecondLimit = 512

    /**
     * Тик игровой логики
     */
    fun update() {
        if (!isPaused) {
            if (Gdx.graphics.deltaTime > 0.1f) Log.i("Slow update: " + Gdx.graphics.deltaTime + " s")
            for (sceneObject in scene.objects) {
                if (sceneObject is Entity) {
                    if (sceneObject.isMoving) {
                        moveEntity(sceneObject, scene)
                    }
                }
            }
        }
    }


    /**
     * Обрабатывает передвижение данного Entity за тик
     */
    private fun moveEntity(entity: Entity, scene: Scene) {
        val range = entity.speed * Time.deltaTime
        for (i in 0 until (range / smallRange).toInt()) {
            smallMoveEntity(entity, smallRange, scene)
        }
        smallMoveEntity(entity, range % smallRange, scene)
    }

    /**
     * Отступ от границы непроходимого тайла
     */
    private val eps = 0.00001f

    /**
     * Обрабатывает короткое (на расстояние не более smallRange) передвижение данного Entity
     * Для обычного передвижения следует использовать moveEntity
     */
    private fun smallMoveEntity(entity: Entity, range: Float, scene: Scene) {
        val newPosition = Point(entity.position.x, entity.position.y)
        newPosition.move(entity.moveAngle, range)
        newPosition.x = Math.max(eps, Math.min(scene.width - eps, newPosition.x))
        newPosition.y = Math.max(eps, Math.min(scene.height - eps, newPosition.y))
        val x = entity.position.x.toInt()
        val y = entity.position.y.toInt()
        val newX = newPosition.x.toInt()
        val newY = newPosition.y.toInt()
        if ((x != newX || y != newY) && !scene.approachabilityMatrix[newX][newY].isWalkable()) {
            if (x == newX) {
                if (y < newY) {
                    newPosition.y = newY - eps
                }
                else {
                    newPosition.y = y + eps
                }
            }
            else if (y == newY) {
                if (x < newX) {
                    newPosition.x = newX - eps
                }
                else {
                    newPosition.x = x + eps
                }
            }
            else {
                val moveVectorX = newX - x
                val moveVectorY = newY - y
                val centerX = Math.max(x, newX)
                val centerY = Math.max(y, newY)
                val backVectorX = centerX - newX
                val backVectorY = centerY - newY
                val orientedSquare = moveVectorX * backVectorY - moveVectorY * backVectorX
                when (true) {
                    (newX > x && newY > y) -> {
                        if (orientedSquare > 0) {
                            when (true) {
                                scene.approachabilityMatrix[newX][y].isWalkable() -> {
                                    newPosition.y = newY - eps
                                }
                                scene.approachabilityMatrix[x][newY].isWalkable() -> {
                                    newPosition.x = newX - eps
                                }
                                else -> {
                                    newPosition.x = newX - eps
                                    newPosition.y = newY - eps
                                }
                            }
                        } else {
                            when (true) {
                                scene.approachabilityMatrix[x][newY].isWalkable() -> {
                                    newPosition.x = newX - eps
                                }
                                scene.approachabilityMatrix[newX][y].isWalkable() -> {
                                    newPosition.y = newY - eps
                                }
                                else -> {
                                    newPosition.x = newX - eps
                                    newPosition.y = newY - eps
                                }
                            }
                        }
                    }
                    (newX < x && newY > y) -> {
                        if (orientedSquare > 0) {
                            when (true) {
                                scene.approachabilityMatrix[x][newY].isWalkable() -> {
                                    newPosition.x = x + eps
                                }
                                scene.approachabilityMatrix[newX][y].isWalkable() -> {
                                    newPosition.y = newY - eps
                                }
                                else -> {
                                    newPosition.x = x + eps
                                    newPosition.y = newY - eps
                                }
                            }
                        } else {
                            when (true) {
                                scene.approachabilityMatrix[newX][y].isWalkable() -> {
                                    newPosition.y = newY - eps
                                }
                                scene.approachabilityMatrix[x][newY].isWalkable() -> {
                                    newPosition.x = x + eps
                                }
                                else -> {
                                    newPosition.x = x + eps
                                    newPosition.y = newY - eps
                                }
                            }
                        }
                    }
                    (newX < x && newY < y) -> {
                        if (orientedSquare > 0) {
                            when (true) {
                                scene.approachabilityMatrix[newX][y].isWalkable() -> {
                                    newPosition.y = y + eps
                                }
                                scene.approachabilityMatrix[x][newY].isWalkable() -> {
                                    newPosition.x = x + eps
                                }
                                else -> {
                                    newPosition.x = x + eps
                                    newPosition.y = y + eps
                                }
                            }
                        } else {
                            when (true) {
                                scene.approachabilityMatrix[x][newY].isWalkable() -> {
                                    newPosition.x = x + eps
                                }
                                scene.approachabilityMatrix[newX][y].isWalkable() -> {
                                    newPosition.y = y + eps
                                }
                                else -> {
                                    newPosition.x = x + eps
                                    newPosition.y = y + eps
                                }
                            }
                        }
                    }
                    (newX > x && newY < y) -> {
                        if (orientedSquare > 0) {
                            when (true) {
                                scene.approachabilityMatrix[x][newY].isWalkable() -> {
                                    newPosition.x = newX - eps
                                }
                                scene.approachabilityMatrix[newX][y].isWalkable() -> {
                                    newPosition.y = y + eps
                                }
                                else -> {
                                    newPosition.x = newX - eps
                                    newPosition.y = y + eps
                                }
                            }
                        } else {
                            when (true) {
                                scene.approachabilityMatrix[newX][y].isWalkable() -> {
                                    newPosition.y = y + eps
                                }
                                scene.approachabilityMatrix[x][newY].isWalkable() -> {
                                    newPosition.x = newX - eps
                                }
                                else -> {
                                    newPosition.x = newX - eps
                                    newPosition.y = y + eps
                                }
                            }
                        }
                    }
                }
            }
        }
        entity.position = newPosition
    }

}