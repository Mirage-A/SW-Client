package com.mirage.gamelogic

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.mirage.utils.Assets
import com.mirage.utils.config
import com.mirage.scriptrunner.logic.MapLogicEventListener
import com.mirage.utils.Log
import com.mirage.utils.MapChangeMessage
import com.mirage.utils.NewObjectMessage
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
            Log.i("Map changed to $map")
            gameLoop.loopLock.lock()
            val oldObjects = gameLoop.objects
            val oldPlayerIDs = gameLoop.playerIDs
            gameLoop.map = newMap
            gameLoop.objects.clear()
            for (id in oldPlayerIDs) {
                val player = oldObjects[id] ?: continue
                gameLoop.objects[id] = player
                gameLoop.sendMessage(NewObjectMessage(id, player))
            }
            for (obj in newMap) {
                if (obj.type != "player") {
                    gameLoop.addNewObject(obj)
                }
            }
            gameLoop.walkabilities = Array(map.properties.getInt("width"))
            {i -> IntArray(map.properties.getInt("height"))
            {j -> (map.layers["walkability"] as TiledMapTileLayer).getCell(i, j).tile.id } }
            gameLoop.logicEventHandler.listeners.clear()
            gameLoop.logicEventHandler.listeners.add(MapLogicEventListener())
            gameLoop.loopLock.unlock()
        }

    /**
     * Загружает карту по данному пути
     * Этот метод не меняет карту на загруженную, нужно вызвать setMap
     */
    fun loadMap(path: String) : TiledMap {
        val map = TmxMapLoader().load("${Assets.assetsPath}maps/$path.tmx")
        for (obj in map) {
            obj.position = getScenePointFromTiledMap(obj.position)
            obj.properties.put("width", obj.properties.getFloat("width") / (config["tile-height"] as? Float ?: 64f))
            obj.properties.put("height", obj.properties.getFloat("height") / (config["tile-height"] as? Float ?: 64f))
        }
        return map
    }

    fun addUpdateTickListener(listener: () -> Unit) {
        gameLoop.updateTickListeners.add(listener)
    }

    fun getObject(id: Long) = gameLoop.objects[id]

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
            name = "player"
            rectangle = spawnPoint.rectangle
            isRigid = true
            speed = spawnPoint.speed
            moveDirection = spawnPoint.moveDirection
            type = "entity"
        }
        val id = gameLoop.addNewObject(player)
        gameLoop.playerIDs.add(id)
        return id
    }

    /**
     * Начать игру (логика на паузе, следует вызвать startLogic)
     */
    fun startGame() {
        //TODO Сделать нормальную реализацию отправки сообщений о карте
        gameLoop.sendMessage(MapChangeMessage("test"))
        map = loadMap("test")
        gameLoop.thread.start()
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