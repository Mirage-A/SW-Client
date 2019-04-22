package com.mirage.utils.messaging

import com.mirage.utils.INTERPOLATION_DELAY_MILLIS
import com.mirage.utils.Log
import com.mirage.utils.MAX_EXTRAPOLATION_INTERVAL
import com.mirage.utils.extensions.*
import com.mirage.utils.gameobjects.GameObjects
import com.mirage.utils.maps.GameStateSnapshot
import java.util.*

/**
 * Данный класс хранит в себе все состояния карты за последнее время и позволяет реализовать интерполяцию состояний.
 */
class SnapshotManager {

    /**
     * Коллекция хранимых состояний
     */
    private val snapshots : NavigableSet<GameStateSnapshot> = TreeSet()


    /**
     * Добавить новое состояние.
     */
    fun addNewSnapshot(snapshot: GameStateSnapshot) = snapshots.add(snapshot)

    /**
     * Создаёт состояние, интерполированное между двумя соседними состояниями
     */
    fun getInterpolatedSnapshot() : GameObjects {
        val renderTime = getRenderTime()
        removeOldSnapshots(renderTime)
        val firstSnapshot = try { snapshots.first() } catch (ex: NoSuchElementException) { null }
        val secondSnapshot = snapshots.second()
        if (firstSnapshot == null) {
            return GameObjects(mapOf(), Long.MIN_VALUE)
        }
        if (secondSnapshot == null) {
            return extrapolateSnapshot(firstSnapshot.finalState, renderTime - firstSnapshot.createdTimeMillis)
        }
        return interpolateSnapshots(firstSnapshot, secondSnapshot, renderTime)
    }

    /**
     * Интерполирует два соседних состояния и возвращает новое.
     */
    private fun interpolateSnapshots(first: GameStateSnapshot, second: GameStateSnapshot, renderTime: Long) : GameObjects {
        if (first.createdTimeMillis > renderTime || second.createdTimeMillis < renderTime) {
            Log.e("INTERPOLATION WARNING firstTime=${first.createdTimeMillis} secondTime=${second.createdTimeMillis} renderTime=$renderTime")
        }
        if (first.createdTimeMillis >= renderTime) return first.finalState
        if (second.createdTimeMillis <= renderTime) return second.finalState
        val progress = (renderTime - first.createdTimeMillis).toFloat() / (second.createdTimeMillis - first.createdTimeMillis).toFloat()
        val objs = first.finalState
        val diff = second.stateDifference
        return objs.update(mapOf()) {id, obj ->
            if (diff.removedObjects.contains(id)) null
            else {
                val newObjDiff = diff.objectDifferences[id]
                if (newObjDiff == null) obj
                else obj.with(
                        x = interpolateValues(obj.x, newObjDiff.x ?: obj.x, progress),
                        y = interpolateValues(obj.y, newObjDiff.y ?: obj.y, progress)
                )
            }
        }
    }

    private fun interpolateValues(first: Float, second: Float, progress: Float) : Float =
            first + (second - first) * progress

    /**
     * Экстраполирует состояние [objs] на min([deltaTimeMillis], [MAX_EXTRAPOLATION_INTERVAL]) мс вперёд.
     */
    private fun extrapolateSnapshot(objs: GameObjects, deltaTimeMillis: Long) : GameObjects {
        if (deltaTimeMillis < 0) return objs
        val delta = Math.min(deltaTimeMillis, MAX_EXTRAPOLATION_INTERVAL)
        return objs.update(mapOf()) {_, obj ->
            if (obj.isMoving) obj.move(delta)
            else obj
        }
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