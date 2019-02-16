package com.mirage.model.scene

import com.mirage.model.datastructures.IntPair
import com.mirage.model.datastructures.Point
import com.mirage.view.Log

object MazeGenerator {
/*
    /**
     * Генерирует сцену - лабиринт размеров (width * 2 + 1) x (height * 2 + 1),
     * где width и height - натуральные
     */
    fun generateMaze(width: Int, height: Int) : Scene {
        val time = System.currentTimeMillis()
        val scene = Scene(width * 2 + 1, height * 2 + 1)
        scene.player = Player(Point(1.5f, 1.5f))
        scene.objects.add(scene.player)
        for (i in 0 until scene.width) {
            for (j in 0 until scene.height) {
                scene.setTileId(i, j, 1)
                scene.setApproachability(i, j, ApproachabilityType.BLOCKED)
            }
        }

        //Используем алгоритм землеройки https://habr.com/ru/post/318530/
        //В описании алгоритма в статье есть ошибка, поэтому алгоритм немного изменен
        while (true) {
            for (i in 0..99) {
                val rndPoint = generateRandomPoint(width, height)

                scene.isTileWalkable(rndPoint)
                jump(rndPoint, scene)
            }
            var allFree = true
            for (i in 0 until width) {
                for (j in 0 until height) {
                    if (!scene.isTileWalkable(i * 2 + 1, j * 2 + 1)) {
                        allFree = false
                    }
                }
            }
            if (allFree) break
        }

        for (i in 0 until scene.width) {
            for (j in 0 until scene.height) {
                if (scene.isTileWalkable(i, j)) {
                    scene.setTileId(i, j, 0)
                }
            }
        }
        Log.i("Maze generated in " + (System.currentTimeMillis() - time) + " ms")
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
                    scene.setApproachability(point.right(), ApproachabilityType.ALL_FREE)
                    scene.setApproachability(point.twiceRight(), ApproachabilityType.ALL_FREE)
                    jump(point.twiceRight(), scene)
                }
                rnd < 0.5f -> if (isJumpable(point.twiceTop(), scene)) {
                    scene.setApproachability(point.top(), ApproachabilityType.ALL_FREE)
                    scene.setApproachability(point.twiceTop(), ApproachabilityType.ALL_FREE)
                    jump(point.twiceTop(), scene)
                }
                rnd < 0.75f -> if (isJumpable(point.twiceLeft(), scene)) {
                    scene.setApproachability(point.left(), ApproachabilityType.ALL_FREE)
                    scene.setApproachability(point.twiceLeft(), ApproachabilityType.ALL_FREE)
                    jump(point.twiceLeft(), scene)
                }
                else -> if (isJumpable(point.twiceBottom(), scene)) {
                    scene.setApproachability(point.bottom(), ApproachabilityType.ALL_FREE)
                    scene.setApproachability(point.twiceBottom(), ApproachabilityType.ALL_FREE)
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
                !scene.isTileWalkable(point))
    }

    private fun generateRandomPoint(width: Int, height: Int) : IntPair {
        return IntPair((Math.random() * width).toInt() * 2 + 1, (Math.random() * height).toInt() * 2 + 1)
    }
*/
}