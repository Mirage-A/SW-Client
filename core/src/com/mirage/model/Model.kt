package com.mirage.model

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.mirage.controller.Platform
import com.mirage.model.datastructures.*
import com.mirage.view.Log
import com.mirage.view.animation.MoveDirection


object Model {
    /**
     * Цикл игровой логики
     */
    private val gameLoop = GameLoop()

    fun setMap(map: TiledMap) {
        gameLoop.map = map
    }

    fun setMap(path: String) {
        gameLoop.map = TmxMapLoader().load(Platform.ASSETS_PATH + path)
        for (obj in gameLoop.map) {
            Log.i(obj.name + " " + obj.getPosition())
            obj.setPosition(getScenePointFromTiledMap(obj.getPosition()))
            Log.i(obj.name + " " + obj.getPosition())
        }
        gameLoop.findPlayer()
        gameLoop.player!!.properties.put("speed", 2.8f)
        setMap(gameLoop.map)
    }


    fun loadMaze(width: Int, height: Int) {
        //TODO setMap(MazeGenerator.generateMaze(width, height))
    }

    fun update() {
        gameLoop.update()
    }

    /**
     * Начать игру (логика на паузе, следует вызвать startLogic)
     */
    fun startGame() {
        setMap("maps/test.tmx")
    }

    /**
     * Получить сцену
     */
    fun getMap() : TiledMap = gameLoop.map

    /**
     * Получить игрока
     */
    fun getPlayer() : MapObject? {
        return gameLoop.player
    }
    /**
     * Получить позицию игрока
     */
    fun getPlayerPosition() : Point {
        return gameLoop.player?.getPosition() ?: Point(0f, 0f)
    }

    /**
     * Задать угол движения (без начала движения)
     */
    fun setMoveAngle(angle: Float) {
        gameLoop.player?.setMoveAngle(angle)
    }

    /**
     * Начать движение персонажа, направление движения задается углом
     */
    fun startMoving(angle: Float) {
        setMoveAngle(angle)
        gameLoop.player?.setMoving(true)
    }

    /**
     * Остановить движение персонажа
     */
    fun stopMoving() {
        gameLoop.player?.setMoving(false)
    }

    /**
     * Возвращает, двигается ли игрок
     */
    fun isPlayerMoving() : Boolean {
        return gameLoop.player?.properties?.getBoolean("isMoving", false) ?: false
    }

    /**
     * Возвращает move direction игрока
     */
    fun getPlayerMoveDirection() : MoveDirection {
        return MoveDirection.fromMoveAngle(gameLoop.player?.properties?.getFloat("moveAngle", 0f) ?: 0f)
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


    /**
     * Переход от кривого базиса карты после загрузки через TmxLoader к базису сцены (тайлы)
     */
    private fun getScenePointFromTiledMap(tiledMapPoint: Point) : Point {
        val x = tiledMapPoint.x / 32f
        val y = tiledMapPoint.y / 32f
        return Point(x, y)
    }
}