package com.mirage.gamelogic

import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.objects.extended.ExtendedEntity
import com.mirage.utils.game.states.ExtendedState
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
private const val SMALL_MOVE_RANGE = 0.1f

/**
 * Отступ от границы непроходимого тайла
 */
internal const val EPS = 0.000001f

/**
 * Функция со всей игровой логикой.
 * Обрабатывает изменения состояния игры за заданное время.
 * Заполняет очередь сообщений [ServerMessage], которые должны быть отправлены ВСЕМ клиентам.
 * Эта коллекция НЕ содержит сообщения об изменении состояния игры.
 * @param delta Интервал в мс, изменение состояния за который обрабатывается.
 * @param state Состояние, изменяемое в течение работы функции
 * @param gameMap Карта игры.
 * @param clientMessages Коллекция пар "id персонажа игрока - сообщение, полученное от этого игрока за время тика",
 *                          итерируемая в порядке получения сообщений.
 * @param serverMessages Очередь, в которую функция будет помещать сообщения для клиентов.
 */
internal fun updateState(
        delta: Long,
        state: ExtendedState,
        gameMap: GameMap,
        clientMessages: Iterable<Pair<Long, ClientMessage>>,
        serverMessages: Queue<ServerMessage>
) {
    //TODO Обработка сообщений от клиентов
    for ((id, msg) in clientMessages) {
        if (msg !is MoveDirectionClientMessage && msg !is SetMovingClientMessage) println("Got client message $id $msg")
        when (msg) {
            is MoveDirectionClientMessage -> {
                state.entities[id]?.moveDirection = msg.md
            }
            is SetMovingClientMessage -> {
                state.entities[id]?.isMoving = msg.isMoving
            }
        }
    }
    for ((_, entity) in state.entities) {
        //TODO Нормальная обработка действий
        entity.action = if (entity.isMoving) "running" else "idle"
        if (entity.isMoving) {
            moveObject(entity, delta, gameMap, state)
        }
    }
    //TODO Обработка логики
}

/**
 * Обрабатывает передвижение сущности за тик.
 * Функция не является чистой, в процессе ее выполнения изменяются координаты [entity]
 */
private fun moveObject(entity: ExtendedEntity, deltaMillis: Long, gameMap: GameMap, state: ExtendedState) {
    val range = entity.speed * deltaMillis.toFloat() / 1000f
    val smallMovesCount = (range / SMALL_MOVE_RANGE).toInt()
    for (i in 0 until smallMovesCount) {
        smallMove(entity, SMALL_MOVE_RANGE, gameMap, state)
    }
    val rangeLeft = range - SMALL_MOVE_RANGE * smallMovesCount
    smallMove(entity, rangeLeft, gameMap, state)
}

/**
 * Обрабатывает короткое (на расстояние не более [SMALL_MOVE_RANGE]) передвижение данного объекта
 * Для обычного передвижения следует использовать [moveObject]
 * Функция не является чистой, в процессе ее выполнения изменяются координаты [entity]
 */
private fun smallMove(entity: ExtendedEntity, range: Float, gameMap: GameMap, state: ExtendedState) {
    var newPosition = entity.position
    newPosition = newPosition.move(entity.moveDirection.toMoveAngle(), range)
    newPosition = Point(
            max(EPS + entity.width / 2, min(gameMap.width - EPS - entity.width / 2, newPosition.x)),
            max(EPS + entity.width / 2, min(gameMap.height - EPS - entity.height / 2, newPosition.y))
    )
    val thisRect = Rectangle(newPosition.x, newPosition.y, entity.width, entity.height)
    if (entity.isRigid) {
        for ((_, otherObj) in state.buildings) {
            val otherRect = Rectangle(otherObj.x, otherObj.y, otherObj.width, otherObj.height)
            if (thisRect.overlaps(otherRect) && otherObj.isRigid) return
        }
        for ((_, otherObj) in state.entities) {
            val otherRect = Rectangle(otherObj.x, otherObj.y, otherObj.width, otherObj.height)
            if (otherObj !== entity && thisRect.overlaps(otherRect) && otherObj.isRigid) return
        }
    }
    for (point in thisRect.points) {
        if (!gameMap.isTileWalkable(point.x.toInt(), point.y.toInt())) {
            return
        }
    }
    entity.x = newPosition.x
    entity.y = newPosition.y
}