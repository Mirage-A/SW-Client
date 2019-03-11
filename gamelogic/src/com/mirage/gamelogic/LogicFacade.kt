package com.mirage.gamelogic

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.mirage.assetmanager.Assets
import com.mirage.utils.config
import com.mirage.scriptrunner.logic.MapLogicEventListener
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.*


class LogicFacade {
    /**
     * Цикл игровой логики
     */
    val gameLoop = GameLoop()

    var map : TiledMap
        get() = gameLoop.map
        set(newMap) {
            gameLoop.map = newMap
            gameLoop.objects.clear()
            for (obj in newMap) {

            }
        }

    fun setMap(path: String) {
        safePauseLogic()
        map = TmxMapLoader().load("${Assets.assetsPath}maps/$path")
        for (obj in map) {
            obj.position = getScenePointFromTiledMap(obj.position)
            obj.properties.put("width", obj.properties.getFloat("width") / (config["tile-height"] as? Float ?: 64f))
            obj.properties.put("height", obj.properties.getFloat("height") / (config["tile-height"] as? Float ?: 64f))
        }
        gameLoop.walkabilities = Array(map.properties.getInt("width"))
            {i -> IntArray(map.properties.getInt("height"))
                {j -> (map.layers["walkability"] as TiledMapTileLayer).getCell(i, j).tile.id } }
        gameLoop.logicEventHandler.listeners.clear()
        gameLoop.logicEventHandler.listeners.add(MapLogicEventListener())
        startLogic()
    }

    fun addUpdateTickListener(listener: () -> Unit) {
        gameLoop.updateTickListeners.add(listener)
    }

    /**
     * Добавить нового игрока и вернуть его id
     * Этот метод является thread-unsafe, поэтому должен вызываться только внутри слушателя updateListener
     */
    fun addNewPlayer() : Long {
        val spawnPoint = findObject("spawn-point") ?: MapObject().apply {
            position = Point(0f, 0f)
            speed = 2.8f
        }
        val player = MapObject().apply {
            rectangle = spawnPoint.rectangle
            isRigid = true
            speed = spawnPoint.speed
            moveDirection = spawnPoint.moveDirection
            type = "player"
        }
        val id = gameLoop.addNewObject(player)
        gameLoop.playerIDs.add(id)
        return id
    }

    /**
     * Начать игру (логика на паузе, следует вызвать startLogic)
     */
    fun startGame() {
        setMap("test.tmx")
        gameLoop.thread.start()
    }

    /**
     * Приостановить цикл логики
     */
    fun pauseLogic() {
        gameLoop.isPaused = true
    }

    /**
     * Приостановить цикл логики и дождаться завершения последней итерации
     */
    fun safePauseLogic() {
        pauseLogic()
        while (gameLoop.isUpdating) {}
    }

    /**
     * Запустить цикл логики или возобновить после паузы
     */
    fun startLogic() {
        gameLoop.isPaused = false
    }

    fun findObject(name: String) : MapObject? {
        for ((_, obj) in gameLoop.objects) {
            if (obj.name == name) return obj
        }
        return null
    }

    fun findAllObjectsByName(name: String) : List<MapObject> {
        val list = ArrayList<MapObject>()
        for ((_, obj) in gameLoop.objects) {
            if (obj.name == name) list.add(obj)
        }
        return list
    }

    fun findAllObjectsByType(type: String) : List<MapObject> {
        val list = ArrayList<MapObject>()
        for ((_, obj) in gameLoop.objects) {
            if (obj.type == type) list.add(obj)
        }
        return list
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