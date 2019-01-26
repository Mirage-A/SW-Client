package com.mirage.model

import com.mirage.model.scene.Point
import com.mirage.model.scene.Scene
import com.mirage.view.animation.MoveDirection
import java.io.File


object Model {
    /**
     * Цикл игровой логики
     */
    private val gameLoop = GameLoop()
    private val loopThread = Thread(gameLoop)

    /**
     * Загрузить карту из файла
     */
    fun loadMapFromFile(map: File) {
        gameLoop.scene.loadMapFromFile(map)
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
        return gameLoop.scene.player.position
    }

    /**
     * Задать угол движения (без начала движения)
     */
    fun setMoveAngle(angle: Float) {
        gameLoop.scene.player.moveAngle = angle
    }

    /**
     * Начать движение персонажа, направление движения задается углом
     */
    fun startMoving(angle: Float) {
        setMoveAngle(angle)
        gameLoop.scene.player.isMoving = true
    }

    /**
     * Остановить движение персонажа
     */
    fun stopMoving() {
        gameLoop.scene.player.isMoving = false
    }

    /**
     * Возвращает, двигается ли игрок
     */
    fun isPlayerMoving() : Boolean {
        return gameLoop.scene.player.isMoving
    }

    /**
     * Возвращает move direction игрока
     */
    fun getPlayerMoveDirection() : MoveDirection {
        return MoveDirection.fromMoveAngle(gameLoop.scene.player.moveAngle)
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