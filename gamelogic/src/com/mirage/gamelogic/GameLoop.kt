package com.mirage.gamelogic

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.AtomicQueue
import com.mirage.scriptrunner.logic.LogicEventHandler
import com.mirage.utils.*
import com.mirage.utils.extensions.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import javax.swing.Timer

class GameLoop {

    /**
     * Мьютекс, который занят во время итерации обновления логики
     */
    val loopLock = ReentrantLock()

    /**
     * Поток, в котором работает этот цикл
     */
    val thread = Thread(Runnable { updateLoop() })

    private var deltaTime = 0f

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
    val objects = TreeMap<Long, MapObject>()
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

    /**
     * Нижний предел времени выполнения итерации
     * (итерация не может выполняться быстрее, это нужно для ограничения кол-ва обновлений в секунду)
     */
    private val minTickTime = 0.01f

    var walkabilities = Array(0) { IntArray(0) }

    private var fps = 0

    private val fpsLogger = Timer(1000) {
        Log.i("game loop updates per second : $fps")
        fps = 0
    }.apply { start() }

    /**
     * Тик игровой логики
     */
    private fun update() {
        for ((id, obj) in objects) {
            if (obj.isMoving) {
                moveObject(id, obj)
            }
        }
        for (listener in updateTickListeners) listener()
        sendMessage(EndOfPackageMessage())
        packagesCount.incrementAndGet()
    }


    /**
     * Добавить новый объект и получить его id
     * Этот метод отправляет сообщение о добавлении объекта
     * Этот метод является thread-unsafe, поэтому должен вызываться только внутри слушателя updateListener
     */
    fun addNewObject(obj: MapObject) : Long {
        objects[nextID] = obj
        sendMessage(NewObjectMessage(nextID, obj))
        return nextID++
    }


    /**
     * Обрабатывает передвижение данного объекта за тик
     */
    private fun moveObject(id: Long, obj: MapObject) {
        val range = obj.speed * deltaTime
        for (i in 0 until (range / smallRange).toInt()) {
            smallMove(obj, smallRange)
        }
        smallMove(obj, range % smallRange)
        sendMessage(MoveObjectMessage(id, obj.position))
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

    private fun updateLoop() {
        var lastTime = -1L
        while (true) {
            if (isPaused) {
                lastTime = -1L
            }
            else {
                loopLock.lock()
                if (lastTime == -1L) {
                    deltaTime = 0f
                    lastTime = System.nanoTime()
                }
                update()
                ++fps
                deltaTime = (System.nanoTime() - lastTime) / 1000000000f
                if (deltaTime < minTickTime) {
                    Thread.sleep(((minTickTime - deltaTime) * 1000f).toLong())
                }
                val cur = System.nanoTime()
                deltaTime = (cur - lastTime) / 1000000000f
                lastTime = cur
                if (deltaTime > 0.1f) Log.i("Slow update: $deltaTime s")
                loopLock.unlock()
            }
        }
    }

    /**
     * Добавить сообщение в очередь сообщений
     * //TODO Здесь используется искусственная задержка, чтобы симулировать обмен данными по сети
     */
    fun sendMessage(msg: UpdateMessage) {
        //GlobalScope.launch {
            //delay(20L)
            queueLock.lock()
            messageQueue.add(msg)
            queueLock.unlock()
        //}
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