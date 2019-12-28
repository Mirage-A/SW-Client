package com.mirage.logic.processors

import com.mirage.core.utils.Point
import com.mirage.core.utils.Rectangle
import com.mirage.core.game.maps.GameMap
import com.mirage.logic.state.ExtendedEntity
import com.mirage.logic.state.ExtendedState
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
 * Обрабатывает передвижение сущности за тик.
 * Функция не является чистой, в процессе ее выполнения изменяются координаты [entity]
 */
internal fun moveObject(entity: ExtendedEntity, deltaMillis: Long, gameMap: GameMap, state: ExtendedState) {
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