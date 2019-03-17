package com.mirage.gamelogic

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Rectangle
import com.mirage.scriptrunner.logic.LogicEventHandler
import com.mirage.utils.*
import com.mirage.utils.Timer
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.*
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.HashMap

internal class GameLoop {

    /**
     * Мьютекс, который занят во время итерации обновления логики
     */
    val loopLock = ReentrantLock()

    /**
     * Поток, в котором работает этот цикл
     */
    val loopTimer = com.mirage.utils.Timer(GAME_LOOP_TICK_INTERVAL, ::update)

    /**
     * Карта
     * Эта карта используется только для работы с тайлами
     * Для работы с объектами следует использовать словарь objects
     */
    var map = TiledMap()

    val logicEventHandler = LogicEventHandler(LogicScriptActionsImpl(this))

    /**
     * Все объекты карты
     */
    val objects : MutableMap<Long, MapObject> = HashMap()
    /**
     * Следующее ID, которое можно использовать при добавлении нового объекта
     * //TODO Рассмотреть невероятный случай, когда за время работы цикла добавилось более 1e18 объектов
     */
    var nextID = Long.MIN_VALUE

    val playerIDs = ArrayList<Long>()

    /**
     * Слушатели, которые выполняются после каждого обновления
     * Эти слушатели выполняются в том же потоке, что и цикл логики
     */
    val updateTickListeners = ArrayList<() -> Unit>()

    /**
     * Очередь, в которую добавляются сообщения при каждом изменении состояния карты
     * Эти сообщения добавляет цикл логики, а обрабатывать и рассылать их клиентам должен сервер
     */
    val messageQueue = ArrayDeque<UpdateMessage>()
    val queueLock = ReentrantLock()
    var packagesCount = AtomicInteger(0)

    /**
     * Этот параметр позволяет приостанавливать логику игры, а затем снова запускать
     */
    var isPaused = true

    /**
     * Перемещение персонажа, которое считается достаточно малым, чтобы при таком перемещении можно было рассматривать только соседние тайлы
     * Длинные перемещения разбиваются на малые такой длины
     */
    private val smallRange = 0.5f

    var walkabilities = Array(0) { IntArray(0) }

    private var fps = 0

    private val fpsLogger = Timer(1000L) {
        Log.i("game loop updates per second : $fps")
        fps = 0
    }// TODO .apply { start() }

    /**
     * Тик игровой логики
     */
    private fun update(deltaMillis: Long) {
        loopLock.lock()
        for ((id, obj) in objects) {
            if (obj.isMoving) {
                moveObject(obj, deltaMillis)
            }
        }
        for (listener in updateTickListeners) listener()
        val positions = HashMap<Long, Point>()
        for ((id, obj) in objects) {
            positions[id] = obj.position
        }
        val moveDirections = HashMap<Long, MoveDirection>()
        for ((id, obj) in objects) {
            moveDirections[id] = obj.moveDirection
        }
        val isMoving = HashMap<Long, Boolean>()
        for ((id, obj) in objects) {
            isMoving[id] = obj.isMoving
        }
        sendMessage(PositionSnapshotMessage(PositionSnapshot(positions, moveDirections, isMoving)))
        sendMessage(EndOfPackageMessage(deltaMillis))
        ++fps
        packagesCount.incrementAndGet()
        if (deltaMillis > 200L) Log.i("Slow update: $deltaMillis ms")
        loopLock.unlock()
    }


    /**
     * Добавить новый объект и получить его id
     * Этот метод отправляет сообщение о добавлении объекта
     */
    fun addNewObject(obj: MapObject) : Long {
        loopLock.lock()
        objects[nextID] = obj
        loopLock.unlock()
        sendMessage(NewObjectMessage(nextID, obj))
        return nextID++
    }


    /**
     * Обрабатывает передвижение данного объекта за тик
     */
    private fun moveObject(obj: MapObject, deltaMillis: Long) {
        val range = obj.speed * deltaMillis.toFloat() / 1000f
        for (i in 0 until (range / smallRange).toInt()) {
            smallMove(obj, smallRange)
        }
        smallMove(obj, range % smallRange)
    }


    fun isTileWalkable(x: Int, y: Int) : Boolean {
         return getTileId(x, y) == 1
    }

    fun isTileShootable(x: Int, y: Int) : Boolean {
        val id = getTileId(x, y)
        return id == 1 || id == 2
    }

    fun getTileId(x: Int, y: Int) : Int {
        return walkabilities[x][y]
    }

    fun setTileId(x: Int, y: Int, id: Int) {
        walkabilities[x][y] = id
    }

    /**
     * Добавить сообщение в очередь сообщений
     */
    fun sendMessage(msg: UpdateMessage) {
        queueLock.lock()
        messageQueue.add(msg)
        queueLock.unlock()
    }

    /**
     * Отступ от границы непроходимого тайла
     */
    private val eps = 0.000001f

    /**
     * Обрабатывает короткое (на расстояние не более smallRange) передвижение данного объекта
     * Для обычного передвижения следует использовать moveEntity
     */
    private fun smallMove(obj: MapObject, range: Float) {
        val rect = obj.rectangle
        val oldPosition = obj.position
        val newPosition = obj.position
        newPosition.move(obj.moveDirection.toAngle(), range)
        newPosition.x = Math.max(eps, Math.min(map.properties.getInt("width", 0) - eps - rect.width, newPosition.x))
        newPosition.y = Math.max(eps, Math.min(map.properties.getInt("height", 0) - eps - rect.height, newPosition.y))
        val newRect = Rectangle(newPosition.x, newPosition.y, rect.width, rect.height)
        //TODO Пересечения объектов
        if (obj.isRigid) {
            for (otherObj in map) {
                if (otherObj != obj && otherObj.rectangle.overlaps(newRect) && otherObj.isRigid) return
            }
        }
        for (point in newRect.points) {
            if (!isTileWalkable(point.x.toInt(), point.y.toInt())) {
                return
            }
        }
        obj.position = newPosition
        logicEventHandler.handleObjectMove(obj, oldPosition, newPosition)
    }

}