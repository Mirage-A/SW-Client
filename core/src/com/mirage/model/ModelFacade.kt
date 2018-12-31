package com.mirage.model

import com.mirage.model.scene.Point
import com.mirage.model.scene.Scene
import java.io.File


class ModelFacade {
    /**
     * Цикл игровой логики
     */
    private val gameLoop = GameLoop()
    private val loopThread = Thread(gameLoop)

    /**
     * Загрузить карту из файла
     */
    fun loadMapFromFile(map: File) {
        gameLoop.player = gameLoop.scene.loadMapFromFile(map)
    }

    /**
     * Начать игру (логика на паузе, следует вызвать startLogic)
     */
    fun startGame() {
        loopThread.start()
    }

    /**
     * Получить сцену
     */
    fun getScene() : Scene = gameLoop.scene

    /**
     * Получить позицию игрока
     */
    fun getPlayerPosition() : Point {
        return gameLoop.player.position
    }

    /**
     * Начать движение персонажа, направление движения задается углом
     */
    fun startMoving(angle: Float) {
        gameLoop.player.moveAngle = angle
        gameLoop.player.isMoving = true
    }

    /**
     * Остановить движение персонажа
     */
    fun stopMoving() {
        gameLoop.player.isMoving = false
    }

    /**
     * Приостановить цикл логики
     */
    fun pauseLogic() {
        gameLoop.isPaused = true
    }

    /**
     * Запустить цикл логики или возобновить после паузы
     */
    fun startLogic() {
        gameLoop.isPaused = false
    }
}