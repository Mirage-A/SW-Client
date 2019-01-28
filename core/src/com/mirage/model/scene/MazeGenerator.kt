package com.mirage.model.scene

import com.mirage.model.datastructures.IntPair
import com.mirage.model.datastructures.Point
import com.mirage.model.scene.objects.entities.Player
import com.mirage.model.datastructures.get
import com.mirage.model.datastructures.set

object MazeGenerator {

    /**
     * Генерирует сцену - лабиринт размеров (width * 2 + 1) x (height * 2 + 1),
     * где width и height - натуральные
     */
    fun generateMaze(width: Int, height: Int) : Scene {
        val scene = Scene()
        scene.width = width * 2 + 1
        scene.height = height * 2 + 1
        scene.player = Player(Point(1.5f, 1.5f))
        scene.objects.add(scene.player)
        scene.tileMatrix = Array(scene.width) {IntArray(scene.height) {1} }
        scene.approachabilityMatrix = Array(scene.width) { Array(scene.height) { ApproachabilityType.BLOCKED } }

        //Используем алгоритм землеройки https://habr.com/ru/post/318530/
        while (true) {
            for (i in 0..99) {
                val rndPoint = generateRandomPoint(width, height)
                scene.approachabilityMatrix[rndPoint] = ApproachabilityType.ALL_FREE
                jump(rndPoint, scene)
            }
            var allFree = true
            for (i in 0 until width) {
                for (j in 0 until height) {
                    if (scene.approachabilityMatrix[i * 2 + 1][j * 2 + 1] != ApproachabilityType.ALL_FREE) {
                        allFree = false
                    }
                }
            }
            if (allFree) break
        }

        for (i in 0 until scene.width) {
            for (j in 0 until scene.height) {
                if (scene.approachabilityMatrix[i][j] == ApproachabilityType.ALL_FREE) {
                    scene.tileMatrix[i][j] = 0
                }
            }
        }

        return scene
    }

    /**
     * Случайный прыжок из данной точки
     */
    private fun jump(point: IntPair, scene: Scene) {
        while (isJumpable(point.twiceTop(), scene) || isJumpable(point.twiceBottom(), scene) ||
                isJumpable(point.twiceLeft(), scene) || isJumpable(point.twiceRight(), scene)) {
            val rnd = Math.random()
            when (true) {
                rnd < 0.25f -> if (isJumpable(point.twiceRight(), scene)) {
                    scene.approachabilityMatrix[point.right()] = ApproachabilityType.ALL_FREE
                    scene.approachabilityMatrix[point.twiceRight()] = ApproachabilityType.ALL_FREE
                    jump(point.twiceRight(), scene)
                }
                rnd < 0.5f -> if (isJumpable(point.twiceTop(), scene)) {
                    scene.approachabilityMatrix[point.top()] = ApproachabilityType.ALL_FREE
                    scene.approachabilityMatrix[point.twiceTop()] = ApproachabilityType.ALL_FREE
                    jump(point.twiceTop(), scene)
                }
                rnd < 0.75f -> if (isJumpable(point.twiceLeft(), scene)) {
                    scene.approachabilityMatrix[point.left()] = ApproachabilityType.ALL_FREE
                    scene.approachabilityMatrix[point.twiceLeft()] = ApproachabilityType.ALL_FREE
                    jump(point.twiceLeft(), scene)
                }
                else -> if (isJumpable(point.twiceBottom(), scene)) {
                    scene.approachabilityMatrix[point.bottom()] = ApproachabilityType.ALL_FREE
                    scene.approachabilityMatrix[point.twiceBottom()] = ApproachabilityType.ALL_FREE
                    jump(point.twiceBottom(), scene)
                }
            }
        }
    }

    /**
     * Можно ли прыгнуть в данную точку (проверка на выход за границы и на проходимость)
     */
    private fun isJumpable(point: IntPair, scene: Scene) : Boolean {
        return (point.x > 0 && point.x < scene.width - 1 && point.y > 0 && point.y < scene.height - 1 &&
                scene.approachabilityMatrix[point] != ApproachabilityType.ALL_FREE)
    }

    private fun generateRandomPoint(width: Int, height: Int) : IntPair {
        return IntPair((Math.random() * width).toInt() * 2 + 1, (Math.random() * height).toInt() * 2 + 1)
    }

}