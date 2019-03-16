package com.mirage.utils

import com.badlogic.gdx.maps.MapObject
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.*
import java.util.*

data class PositionSnapshot (
        val positions: Map<Long, Point>,
        val createdTimeMillis : Long = System.currentTimeMillis()
        ) : Comparable<PositionSnapshot>, Iterable<Map.Entry<Long, Point>> {


    /**
     * Сравнивает состояния по времени создания
     */
    override fun compareTo(other: PositionSnapshot): Int = when {
        createdTimeMillis > other.createdTimeMillis -> 1
        createdTimeMillis == other.createdTimeMillis -> 0
        else -> -1
    }

    /**
     * Итератор по объектам карты
     */
    override fun iterator() = positions.iterator()

    /**
     * Клонирует данное состояние и создаёт новое состояние.
     * Если [changeCreatedTime] равен true,
     * то [createdTimeMillis] клонированного состояния устанавливается на время создания копии, а не оригинала.
     * Иначе [changeCreatedTime] равен времени создания оригинала.
     */
    fun clone(changeCreatedTime: Boolean = false) : PositionSnapshot {
        val newPositions = TreeMap<Long, Point>()
        for ((id, pos) in positions) {
            newPositions[id] = Point(pos.x, pos.y)
        }
        return PositionSnapshot(newPositions, if (changeCreatedTime) System.currentTimeMillis() else createdTimeMillis)
    }

}
