package com.mirage.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.mirage.model.datastructures.*
import com.mirage.model.scripts.ScriptLoader
import com.mirage.view.Log

class GameLoop {
    var map = TiledMap()

    var player: MapObject? = null

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
            for (layer in map.layers) {
                for (obj in layer.objects) {
                    if (obj.name == "player") {
                        if (obj.properties.getBoolean("isMoving", false)) {
                            moveEntity(obj)
                        }
                    }
                }
            }
        }
    }


    /**
     * Обрабатывает передвижение данного объекта за тик
     */
    private fun moveEntity(obj: MapObject) {
        val range = obj.properties.getFloat("speed", 0f) * Time.deltaTime
        for (i in 0 until (range / smallRange).toInt()) {
            smallMoveEntity(obj, smallRange)
        }
        smallMoveEntity(obj, range % smallRange)
    }

    /**
     * Отступ от границы непроходимого тайла
     */
    private val eps = 0.00001f

    /**
     * Обрабатывает короткое (на расстояние не более smallRange) передвижение данного объекта
     * Для обычного передвижения следует использовать moveEntity
     */
    private fun smallMoveEntity(obj: MapObject, range: Float) {
        val newPosition = obj.getPosition()
        newPosition.move(obj.getMoveAngle(), range)
        newPosition.x = Math.max(eps, Math.min(map.properties.getInt("width", 0) - eps, newPosition.x))
        newPosition.y = Math.max(eps, Math.min(map.properties.getInt("height", 0) - eps, newPosition.y))
        val x = obj.getPosition().x.toInt()
        val y = obj.getPosition().y.toInt()
        val newX = newPosition.x.toInt()
        val newY = newPosition.y.toInt()
        if ((x != newX || y != newY) && !isTileWalkable(newX, newY)) {
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
                                isTileWalkable(newX, y) -> {
                                    newPosition.y = newY - eps
                                }
                                isTileWalkable(x, newY) -> {
                                    newPosition.x = newX - eps
                                }
                                else -> {
                                    newPosition.x = newX - eps
                                    newPosition.y = newY - eps
                                }
                            }
                        } else {
                            when (true) {
                                isTileWalkable(x, newY) -> {
                                    newPosition.x = newX - eps
                                }
                                isTileWalkable(newX, y) -> {
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
                                isTileWalkable(x, newY) -> {
                                    newPosition.x = x + eps
                                }
                                isTileWalkable(newX, y) -> {
                                    newPosition.y = newY - eps
                                }
                                else -> {
                                    newPosition.x = x + eps
                                    newPosition.y = newY - eps
                                }
                            }
                        } else {
                            when (true) {
                                isTileWalkable(newX, y) -> {
                                    newPosition.y = newY - eps
                                }
                                isTileWalkable(x, newY) -> {
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
                                isTileWalkable(newX, y) -> {
                                    newPosition.y = y + eps
                                }
                                isTileWalkable(x, newY) -> {
                                    newPosition.x = x + eps
                                }
                                else -> {
                                    newPosition.x = x + eps
                                    newPosition.y = y + eps
                                }
                            }
                        } else {
                            when (true) {
                                isTileWalkable(x, newY) -> {
                                    newPosition.x = x + eps
                                }
                                isTileWalkable(newX, y) -> {
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
                                isTileWalkable(x, newY) -> {
                                    newPosition.x = newX - eps
                                }
                                isTileWalkable(newX, y) -> {
                                    newPosition.y = y + eps
                                }
                                else -> {
                                    newPosition.x = newX - eps
                                    newPosition.y = y + eps
                                }
                            }
                        } else {
                            when (true) {
                                isTileWalkable(newX, y) -> {
                                    newPosition.y = y + eps
                                }
                                isTileWalkable(x, newY) -> {
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
        obj.setPosition(newPosition)
    }

    /**
     * Найти игрока среди объектов
     */
    fun findPlayer() {
        player = map.findObject("player")
    }

    fun isTileWalkable(x: Int, y: Int) : Boolean {
        //TODO
        return true
    }

}