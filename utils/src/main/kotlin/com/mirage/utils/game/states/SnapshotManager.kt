package com.mirage.utils.game.states

import com.mirage.utils.INTERPOLATION_DELAY_MILLIS
import com.mirage.utils.Log
import com.mirage.utils.MAX_EXTRAPOLATION_INTERVAL
import com.mirage.utils.extensions.second
import com.mirage.utils.game.objects.GameObjects
import java.util.*
import kotlin.math.min

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
     * @param currentTimeMillis Текущее время в мс.
     * @return Состояние на момент [currentTimeMillis] - [INTERPOLATION_DELAY_MILLIS],
     * что позволяет компенсировать задержку в обмене данными с сервером.
     */
    fun getInterpolatedSnapshot(currentTimeMillis: Long) : GameObjects {
        val renderTime = currentTimeMillis - INTERPOLATION_DELAY_MILLIS
        removeOldSnapshots(renderTime)
        val firstSnapshot = try { snapshots.first() } catch (ex: NoSuchElementException) { null }
        val secondSnapshot = snapshots.second()
        if (firstSnapshot == null) {
            Log.d("Initial state is not still received")
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
        /*if (first.createdTimeMillis > renderTime || second.createdTimeMillis < renderTime) {
            Log.e("INTERPOLATION WARNING firstTime=${first.createdTimeMillis} secondTime=${second.createdTimeMillis} renderTime=$renderTime")
        }*/
        if (first.createdTimeMillis >= renderTime) return first.finalState
        if (second.createdTimeMillis <= renderTime) return second.finalState
        val progress = (renderTime - first.createdTimeMillis).toFloat() / (second.createdTimeMillis - first.createdTimeMillis).toFloat()
        val objs = first.finalState
        val diff = second.stateDifference
        return objs.update(mapOf()) {id, obj ->
            if (diff.removedObjects.contains(id)) null
            else {
                val newObj = diff.changedObjects[id]
                if (newObj == null) obj
                else obj.with(
                        x = interpolateValues(obj.x, newObj.x, progress),
                        y = interpolateValues(obj.y, newObj.y, progress)
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
        val delta = min(deltaTimeMillis, MAX_EXTRAPOLATION_INTERVAL)
        return objs.update(mapOf()) {_, obj ->
            if (obj.isMoving) obj.move(delta)
            else obj
        }
    }

    /**
     * Удаляет состояния, которые уже никогда не будут использоваться.
     */
    private fun removeOldSnapshots(renderTime: Long) {
        while (snapshots.second()?.createdTimeMillis ?: Long.MAX_VALUE < renderTime) {
            snapshots.pollFirst()
        }
    }


}