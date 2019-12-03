package com.mirage.utils.game.states

import com.mirage.utils.INTERPOLATION_DELAY_MILLIS
import com.mirage.utils.Log
import com.mirage.utils.MAX_EXTRAPOLATION_INTERVAL
import com.mirage.utils.extensions.second
import com.mirage.utils.game.objects.simplified.SimplifiedEntity
import com.mirage.utils.game.objects.properties.MoveDirection
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
    fun getInterpolatedSnapshot(currentTimeMillis: Long) : SimplifiedState {
        val renderTime = currentTimeMillis - INTERPOLATION_DELAY_MILLIS
        removeOldSnapshots(renderTime)
        val firstSnapshot = try { snapshots.first() } catch (ex: NoSuchElementException) { null }
        val secondSnapshot = snapshots.second()
        if (firstSnapshot == null) {
            Log.d("Initial state is not still received")
            return SimplifiedState()
        }
        if (secondSnapshot == null) {
            return extrapolateSnapshot(firstSnapshot.finalState, renderTime - firstSnapshot.createdTimeMillis)
        }
        return interpolateSnapshots(firstSnapshot, secondSnapshot, renderTime)
    }

    /**
     * Интерполирует два соседних состояния и возвращает новое.
     */
    @Suppress("DuplicatedCode")
    private fun interpolateSnapshots(first: GameStateSnapshot, second: GameStateSnapshot, renderTime: Long) : SimplifiedState {
        if (first.createdTimeMillis >= renderTime) return first.finalState
        if (second.createdTimeMillis <= renderTime) return second.finalState
        val progress = (renderTime - first.createdTimeMillis).toFloat() / (second.createdTimeMillis - first.createdTimeMillis).toFloat()
        val initialState = first.finalState
        val diff = second.stateDifference
        return SimplifiedState(
                //TODO Как-то избавиться от дубликации кода, хотя здесь это сложно
                buildings = initialState.buildings
                        .filterKeys { !diff.buildingsDifference.removed.contains(it) }
                        .mapValues {
                            val newObj = diff.buildingsDifference.changed[it.key]
                            if (newObj == null) it.value
                            else it.value.with(
                                    x = interpolateValues(it.value.x, newObj.x, progress),
                                    y = interpolateValues(it.value.y, newObj.y, progress)
                            )
                        },
                entities = initialState.entities
                        .filterKeys { !diff.entitiesDifference.removed.contains(it) }
                        .mapValues {
                            val newObj = diff.entitiesDifference.changed[it.key]
                            if (newObj == null) it.value
                            else it.value.with(
                                    x = interpolateValues(it.value.x, newObj.x, progress),
                                    y = interpolateValues(it.value.y, newObj.y, progress)
                            )
                        }
        )
    }


    private fun interpolateValues(first: Float, second: Float, progress: Float) : Float =
            first + (second - first) * progress

    private fun SimplifiedEntity.extrapolateMovingFor(deltaTimeMillis: Long) : SimplifiedEntity =
            if (this.isMoving) this.with(
                    x = this.x + this.speed * when (this.moveDirection) {
                        MoveDirection.UP, MoveDirection.DOWN -> 0f
                        MoveDirection.LEFT -> -1f
                        MoveDirection.RIGHT -> 1f
                        MoveDirection.UP_RIGHT, MoveDirection.DOWN_RIGHT -> 1.41421f
                        MoveDirection.UP_LEFT, MoveDirection.DOWN_LEFT -> -1.41421f
                    } * deltaTimeMillis / 1000L,
                    y = this.y + this.speed * when (this.moveDirection) {
                        MoveDirection.LEFT, MoveDirection.RIGHT -> 0f
                        MoveDirection.DOWN -> -1f
                        MoveDirection.UP -> 1f
                        MoveDirection.UP_RIGHT, MoveDirection.UP_LEFT -> 1.41421f
                        MoveDirection.DOWN_RIGHT, MoveDirection.DOWN_LEFT -> -1.41421f
                    } * deltaTimeMillis / 1000L
            )
            else this

    //TODO Экстраполяция движения снарядов

    /**
     * Экстраполирует состояние [state] на min([deltaTimeMillis], [MAX_EXTRAPOLATION_INTERVAL]) мс вперёд.
     */
    private fun extrapolateSnapshot(state: SimplifiedState, deltaTimeMillis: Long) : SimplifiedState {
        if (deltaTimeMillis < 0) return state
        val delta = min(deltaTimeMillis, MAX_EXTRAPOLATION_INTERVAL)
        return SimplifiedState(
                buildings = state.buildings,
                entities = state.entities.mapValues { it.value.extrapolateMovingFor(delta) }
        )
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