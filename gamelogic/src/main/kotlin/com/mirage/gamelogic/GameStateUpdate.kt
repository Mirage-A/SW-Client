package com.mirage.gamelogic

import com.badlogic.gdx.math.Rectangle
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.points
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.game.objects.MutableGameObject
import com.mirage.utils.game.objects.MutableGameObjects
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.MoveDirectionClientMessage
import com.mirage.utils.messaging.ServerMessage
import com.mirage.utils.messaging.SetMovingClientMessage
import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * Перемещение персонажа, которое считается достаточно малым, чтобы при таком перемещении можно было рассматривать только соседние тайлы
 * Длинные перемещения разбиваются на малые такой длины
 */
private const val SMALL_MOVE_RANGE = 0.5f

/**
 * Отступ от границы непроходимого тайла
 */
internal const val EPS = 0.000001f

/**
 * Функция со всей игровой логикой.
 * Обрабатывает изменения состояния игры за заданное время.
 * Возвращает новое ИЗМЕНЯЕМОЕ состояние игры и очередь сообщений [ServerMessage], которые должны быть отправлены ВСЕМ клиентам.
 * Эта коллекция НЕ содержит сообщения об изменении состояния игры.
 * @param delta Интервал в мс, изменение состояния за который обрабатывается.
 * @param originState Изначальное состояние игры.
 * @param gameMap Карта игры.
 * @param clientMessages Коллекция пар "id персонажа игрока - сообщение, полученное от этого игрока за время тика",
 *                          итерируемая в порядке получения сообщений.
 */
internal fun updateState(
        delta: Long,
        originState: GameObjects,
        gameMap: GameMap,
        clientMessages: Iterable<Pair<Long, ClientMessage>>
) : Pair<MutableGameObjects, ArrayDeque<ServerMessage>> {
    val objs = originState.mutableCopy()
    val serverMessages : ArrayDeque<ServerMessage> = ArrayDeque()
    //TODO Обработка сообщений от клиентов
    for ((id, msg) in clientMessages) {
        if (msg !is MoveDirectionClientMessage && msg !is SetMovingClientMessage) println("Got client message $id $msg")
        when (msg) {
            is MoveDirectionClientMessage -> {
                objs[id]?.moveDirection = msg.md
            }
            is SetMovingClientMessage -> {
                objs[id]?.isMoving = msg.isMoving
            }
        }
    }
    for ((_, obj) in objs) {
        //TODO Нормальная обработка действий
        obj.action = if (obj.isMoving) "RUNNING" else "IDLE"
        if (obj.isMoving) {
            moveObject(obj, delta, gameMap, objs)
        }
    }
    //TODO Обработка логики

    return Pair(objs, serverMessages)
}

/**
 * Обрабатывает передвижение данного объекта за тик.
 * Функция не является чистой, в процессе ее выполнения изменяются координаты [obj]
 */
private fun moveObject(obj: MutableGameObject, deltaMillis: Long, gameMap: GameMap, gameObjects: MutableGameObjects) {
    val range = obj.speed * deltaMillis.toFloat() / 1000f
    for (i in 0 until (range / SMALL_MOVE_RANGE).toInt()) {
        smallMove(obj, SMALL_MOVE_RANGE, gameMap, gameObjects)
    }
    smallMove(obj, range % SMALL_MOVE_RANGE, gameMap, gameObjects)
}

/**
 * Обрабатывает короткое (на расстояние не более [SMALL_MOVE_RANGE]) передвижение данного объекта
 * Для обычного передвижения следует использовать [moveObject]
 * Функция не является чистой, в процессе ее выполнения изменяются координаты [obj]
 */
private fun smallMove(obj: MutableGameObject, range: Float, gameMap: GameMap, gameObjects: MutableGameObjects) {
    var newPosition = obj.position
    newPosition = newPosition.move(obj.moveDirection.toMoveAngle(), range)
    newPosition = Point(
            max(EPS + obj.width / 2, min(gameMap.width - EPS - obj.width / 2, newPosition.x)),
            max(EPS + obj.width / 2, min(gameMap.height - EPS - obj.height / 2, newPosition.y))
    )
    //TODO Пересечения объектов
    val thisRect = Rectangle(newPosition.x - obj.width / 2, newPosition.y - obj.height / 2, obj.width, obj.height)
    if (obj.isRigid) {
        for ((_, otherObj) in gameObjects) {
            //Сравнение по ссылке
            val otherRect = Rectangle(otherObj.x - otherObj.width / 2, otherObj.y - otherObj.height / 2, otherObj.width, otherObj.height)
            if (otherObj !== obj && thisRect.overlaps(otherRect) && otherObj.isRigid) return
        }
    }
    for (point in thisRect.points) {
        if (!gameMap.isTileWalkable(point.x.toInt(), point.y.toInt())) {
            return
        }
    }
    obj.x = newPosition.x
    obj.y = newPosition.y
}