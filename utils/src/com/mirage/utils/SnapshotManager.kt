package com.mirage.utils

import com.badlogic.gdx.maps.MapObject
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.*
import java.util.*

/**
 * Данный класс хранит в себе все состояния карты за последнее время и позволяет реализовать интерполяцию состояний.
 */
class SnapshotManager {

    /**
     * Коллекция хранимых состояний
     */
    private val snapshots : NavigableSet<PositionSnapshot> = TreeSet()

    /**
     * Добавить новое состояние.
     */
    fun addNewSnapshot(snapshot: PositionSnapshot) = snapshots.add(snapshot)

    /**
     * Создаёт состояние, интерполированное между двумя соседними состояниями
     */
    fun getInterpolatedSnapshot() : PositionSnapshot {
        val renderTime = getRenderTime()
        removeOldSnapshots(renderTime)
        val firstSnapshot = try { snapshots.first() } catch (ex: NoSuchElementException) { null }
        val secondSnapshot = snapshots.second()
        if (firstSnapshot == null) return PositionSnapshot(TreeMap())
        if (secondSnapshot == null) return extrapolateSnapshot(firstSnapshot, renderTime - firstSnapshot.createdTimeMillis)
        return interpolateSnapshots(firstSnapshot, secondSnapshot, renderTime)
    }

    /**
     * Интерполирует два соседних состояния и возвращает новое.
     */
    private fun interpolateSnapshots(first: PositionSnapshot, second: PositionSnapshot, renderTime: Long) : PositionSnapshot {
        if (first.createdTimeMillis > renderTime || second.createdTimeMillis < renderTime) {
            Log.e("INTERPOLATION ERROR firstTime=${first.createdTimeMillis} secondTime=${second.createdTimeMillis} renderTime=$renderTime")
        }
        val progress = (renderTime - first.createdTimeMillis).toFloat() / (second.createdTimeMillis - first.createdTimeMillis).toFloat()
        val newPositions = TreeMap<Long, Point>()
        for ((id, pos) in first.positions) {
            newPositions[id] = interpolatePositions(pos, second.positions[id], progress)
        }
        return PositionSnapshot(newPositions)
    }

    private fun interpolatePositions(first: Point?, second: Point?, progress: Float) : Point {
        first ?: return DEFAULT_MAP_POINT
        second ?: return DEFAULT_MAP_POINT
        return Point(first.x + (second.x - first.x) * progress, first.y + (second.y - first.y) * progress)
    }

    /**
     * //TODO
     * Экстраполирует состояние [snapshot] на max([deltaTimeMillis], [MAX_EXTRAPOLATION_INTERVAL]) мс вперёд.
     * Возвращается новое состояние.
     */
    private fun extrapolateSnapshot(snapshot: PositionSnapshot, deltaTimeMillis: Long) : PositionSnapshot {
        Log.i("extrapolate")
        return snapshot.clone()
    }

    /**
     * Серверное время, которое в данный момент должен отображать клиент.
     * В задержку времени входит и пинг, и задержка для интерполяции.
     */
    private fun getRenderTime() = System.currentTimeMillis() - INTERPOLATION_DELAY_MILLIS

    /**
     * Удаляет состояния, которые уже никогда не будут использоваться.
     */
    private fun removeOldSnapshots(renderTime: Long) {
        while (snapshots.second()?.createdTimeMillis ?: Long.MAX_VALUE < renderTime) {
            snapshots.pollFirst()
        }
    }


}