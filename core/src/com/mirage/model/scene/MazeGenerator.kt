package com.mirage.model.scene

import com.mirage.model.scene.objects.entities.Player

object MazeGenerator {

    /**
     * Генерирует сцену - лабиринт заданных размеров
     */
    fun generateMaze(width: Int, height: Int) : Scene {
        val scene = Scene()
        scene.width = width
        scene.height = height
        scene.player = Player(Point(1.5f, 1.5f))
        scene.objects.add(scene.player)
        scene.tileMatrix = Array(width) {IntArray(height) {0} }
        scene.approachabilityMatrix = Array(width) { Array(height) { ApproachabilityType.ALL_FREE } }
        for (i in 0 until width) {
            scene.tileMatrix[i][0] = 1
            scene.tileMatrix[i][height - 1] = 1
        }
        for (j in 0 until height) {
            scene.tileMatrix[0][j] = 1
            scene.tileMatrix[width - 1][j] = 1
        }

        // Здесь мы заполняем tileMatrix
        // У нас есть СНМ на проходимых клетках
        // По умолчанию проходимы только начальная и конечная клетки
        // Мы рандомно открываем закрытые клетки так, чтобы не появлялось циклов
        // В итоге получим одну компоненту связности - это и есть лабиринт,
        // в котором гарантированно есть путь



        for (i in 0 until width) {
            for (j in 0 until height) {
                if (scene.tileMatrix[i][j] == 1) {
                    scene.approachabilityMatrix[i][j] = ApproachabilityType.BLOCKED
                }
            }
        }

        return scene
    }
}