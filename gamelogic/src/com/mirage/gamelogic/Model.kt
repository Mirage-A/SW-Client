package com.mirage.gamelogic

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.mirage.assetmanager.Assets
import com.mirage.configuration.config
import com.mirage.scriptrunner.logic.MapLogicEventListener
import com.mirage.utils.MoveDirection
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.*


object Model {
    /**
     * Цикл игровой логики
     */
    val gameLoop = GameLoop()

    fun setMap(map: TiledMap) {
        gameLoop.map = map
    }

    fun setMap(path: String) {
        gameLoop.map = TmxMapLoader().load("${Assets.assetsPath}maps/$path")
        for (obj in gameLoop.map) {
            obj.position = getScenePointFromTiledMap(obj.position)
            obj.properties.put("width", obj.properties.getFloat("width") / (config["tile-height"] as? Float ?: 64f))
            obj.properties.put("height", obj.properties.getFloat("height") / (config["tile-height"] as? Float ?: 64f))
        }
        gameLoop.findPlayer()
        setMap(gameLoop.map)
        gameLoop.walkabilities = Array(gameLoop.map.properties.getInt("width"))
            {i -> IntArray(gameLoop.map.properties.getInt("height"))
                {j -> (gameLoop.map.layers["walkability"] as TiledMapTileLayer).getCell(i, j).tile.id } }
        gameLoop.logicEventHandler.listeners.clear()
        gameLoop.logicEventHandler.listeners.add(MapLogicEventListener())
    }


    fun update() {
        gameLoop.update()
    }

    /**
     * Начать игру (логика на паузе, следует вызвать startLogic)
     */
    fun startGame() {
        setMap("test.tmx")
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
        return gameLoop.player?.position ?: Point(0f, 0f)
    }

    /**
     * Задать угол движения (без начала движения)
     */
    fun setMoveAngle(angle: Float) {
        gameLoop.player?.moveAngle = angle
    }

    fun getMoveAngle() : Float = gameLoop.player?.moveAngle ?: 0f

    /**
     * Начать движение персонажа, направление движения задается углом
     */
    fun startMoving(angle: Float) {
        setMoveAngle(angle)
        gameLoop.player?.isMoving = true
    }

    fun startMoving() {
        gameLoop.player?.isMoving = true
    }
    /**
     * Остановить движение персонажа
     */
    fun stopMoving() {
        gameLoop.player?.isMoving = false
    }

    /**
     * Возвращает, двигается ли игрок
     */
    fun isPlayerMoving() : Boolean {
        return gameLoop.player?.isMoving ?: false
    }

    /**
     * Возвращает move direction игрока
     */
    fun getPlayerMoveDirection() : MoveDirection {
        return MoveDirection.fromMoveAngle(gameLoop.player?.moveAngle ?: 0f)
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

    fun findObject(name: String) : MapObject? {
        for (obj in gameLoop.map) {
            if (obj.name == name) return obj
        }
        return null
    }

    /**
     * Переход от кривого базиса карты после загрузки через TmxLoader к базису сцены (тайлы)
     */
    private fun getScenePointFromTiledMap(tiledMapPoint: Point) : Point {
        val x = tiledMapPoint.x / 64f
        val y = tiledMapPoint.y / 64f
        return Point(x, y)
    }

}