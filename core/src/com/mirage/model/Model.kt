package com.mirage.model

import com.mirage.model.datastructures.Point
import com.mirage.model.scene.MazeGenerator
import com.mirage.model.scene.Scene
import com.mirage.view.animation.MoveDirection


object Model {
    /**
     * Цикл игровой логики
     */
    private val gameLoop = GameLoop()
    private val loopThread = Thread(gameLoop)

    /**
     * Переходит на другую сцену и возвращает старую сцену
     * При этом, если цикл был приостановлен, то он не возобновляется, иначе продолжает работать
     */
    fun setScene(scene: Scene) : Scene{
        val wasPaused = gameLoop.isPaused
        gameLoop.pauseAndAwait()
        val tmp = gameLoop.scene
        gameLoop.scene = scene
        if (!wasPaused) {
            startLogic()
        }
        return tmp
    }

    fun loadMaze(width: Int, height: Int) {
        setScene(MazeGenerator.generateMaze(width, height))
    }

    /**
     * Начать игру (логика на паузе, следует вызвать startLogic)
     */
    fun startGame() {
        loadMaze(10, 10)
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