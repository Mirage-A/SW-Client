package com.mirage.utils.messaging

import com.mirage.utils.DEFAULT_MAP_POINT
import com.mirage.utils.INTERPOLATION_DELAY_MILLIS
import com.mirage.utils.Log
import com.mirage.utils.MAX_EXTRAPOLATION_INTERVAL
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.*
import java.util.*
import kotlin.collections.HashMap

/**
 * Данный класс хранит в себе все состояния карты за последнее время и позволяет реализовать интерполяцию состояний.
 */
class SnapshotManager(private val state: GameState) {

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
        if (firstSnapshot == null) {
            return PositionSnapshot(HashMap(), HashMap(), HashMap())
        }
        if (secondSnapshot == null) {
            return extrapolateSnapshot(firstSnapshot, renderTime - firstSnapshot.createdTimeMillis)
        }
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
        val newPositions = HashMap<Long, Point>()
        for ((id, pos) in first.positions) {
            newPositions[id] = interpolatePositions(pos, second.positions[id], progress)
        }
        val newMoveDirections = HashMap<Long, MoveDirection>()
        for ((id, md) in first.moveDirections) {
            //TODO плавная смена moveDirection-а
            // newMoveDirections[id] = interpolateMoveDirections(md, second.moveDirections[id], progress)
            newMoveDirections[id] = md
        }
        val newMoving = HashMap<Long, Boolean>()
        for ((id, moving) in first.isMoving) {
            newMoving[id] = moving
        }
        return PositionSnapshot(newPositions, newMoveDirections, newMoving)
    }

    private fun interpolatePositions(first: Point?, second: Point?, progress: Float) : Point {
        first ?: return DEFAULT_MAP_POINT
        second ?: return DEFAULT_MAP_POINT
        return Point(first.x + (second.x - first.x) * progress, first.y + (second.y - first.y) * progress)
    }

    private fun interpolateMoveDirections(first: MoveDirection?, second: MoveDirection?, progress: Float) : MoveDirection {
        first ?: return second ?: MoveDirection.DOWN
        second ?: return first
        val angle1 = first.toAngle()
        val angle2 = second.toAngle()
        return MoveDirection.fromMoveAngle(angle1 + (angle2 - angle1) * progress)
    }

    /**
     * Экстраполирует состояние [snapshot] на min([deltaTimeMillis], [MAX_EXTRAPOLATION_INTERVAL]) мс вперёд.
     * Возвращается новое состояние.
     */
    private fun extrapolateSnapshot(snapshot: PositionSnapshot, deltaTimeMillis: Long) : PositionSnapshot {
        if (deltaTimeMillis < 0) return snapshot.clone()
        val delta = Math.min(deltaTimeMillis, MAX_EXTRAPOLATION_INTERVAL)
        val newSnapshot = snapshot.clone()
        for ((id, moving) in snapshot.isMoving) {
            if (moving) {
                newSnapshot.positions[id]?.move(newSnapshot.moveDirections[id]?.toAngle() ?: 0f, (state.objects[id]?.speed ?: 0f) * delta / 1000f)
            }
        }
        return newSnapshot
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