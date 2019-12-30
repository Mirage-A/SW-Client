package com.mirage.ui.fragments.gameview

import com.mirage.core.game.objects.*
import com.mirage.core.utils.TimeMillis
import com.mirage.core.game.objects.properties.MoveDirection
import java.util.*
import kotlin.math.min

const val INTERPOLATION_DELAY_MILLIS = 250L

const val MAX_EXTRAPOLATION_INTERVAL = 250L

private data class Snapshot(
        val stateDifference: StateDifference,
        val createdTimeMillis: TimeMillis
) : Comparable<Snapshot> {
    override fun compareTo(other: Snapshot): Int = createdTimeMillis.compareTo(other.createdTimeMillis)
}

/** Contains latest game states and performs interpolation and extrapolation of game states for following rendering */
internal class SnapshotManager(
        /** This method is invoked when new snapshot is being rendered */
        private val onNextSnapshot: (oldState: SimplifiedState, difference: StateDifference) -> Unit = { _, _ -> }
) {

    /**
     * List of actual snapshots, sorted by their creation time from earliest to latest.
     * Since snapshot count will be very low, sorted collections are not needed.
     * */
    private val snapshots: MutableList<Snapshot> = ArrayList()


    /** Starting state. Differences from [snapshots] will be applied to this state */
    private var initialState: SimplifiedState = SimplifiedState()
    private var initialStateCreatedTimeMillis: TimeMillis = 0L

    /** Sets initial state, clearing all previous information */
    fun setInitialState(initialState: SimplifiedState, createdTimeMillis: TimeMillis) {
        this.initialState = initialState
        initialStateCreatedTimeMillis = createdTimeMillis
        snapshots.clear()
        snapshots.add(Snapshot(StateDifference(), createdTimeMillis))
    }

    /** Adds new snapshot to buffer */
    fun addSnapshot(difference: StateDifference, createdTimeMillis: TimeMillis) {
        val snapshot = Snapshot(difference, createdTimeMillis)
        var index = snapshots.binarySearch(snapshot)
        if (index < 0) index = -index - 1
        snapshots.add(index, snapshot)
    }

    /**
     * Creates a state interpolated between two nearest states at a moment [currentTimeMillis] - [INTERPOLATION_DELAY_MILLIS],
     * which helps to deal with remote connection delay.
     */
    fun getInterpolatedSnapshot(currentTimeMillis: Long): SimplifiedState {
        val renderTime = currentTimeMillis - INTERPOLATION_DELAY_MILLIS
        removeOldSnapshots(renderTime)
        if (snapshots.isEmpty()) return extrapolateSnapshot(initialState, renderTime - initialStateCreatedTimeMillis)
        val progress = (renderTime - initialStateCreatedTimeMillis).toFloat() /
                (snapshots[0].createdTimeMillis - initialStateCreatedTimeMillis).toFloat()
        return interpolateSnapshots(initialState, snapshots[0].stateDifference, progress)
    }


    /** Removes old states which will never be used anymore */
    private fun removeOldSnapshots(renderTime: Long) {
        while (snapshots.isNotEmpty() && snapshots[0].createdTimeMillis < renderTime) {
            val oldState = initialState
            val difference = snapshots[0].stateDifference
            val newState = difference.projectOn(oldState)
            initialState = newState
            initialStateCreatedTimeMillis = snapshots[0].createdTimeMillis
            snapshots.removeAt(0)
            onNextSnapshot(oldState, difference)
        }
    }

}


/** Interpolates two states */
private fun interpolateSnapshots(initialState: SimplifiedState, difference: StateDifference, progress: Float): SimplifiedState {
    if (progress < 0f) return initialState
    if (progress > 1f) return difference.projectOn(initialState)
    return SimplifiedState(
            buildings = interpolateObjects(initialState.buildings, difference.buildingsDifference, progress),
            entities = interpolateObjects(initialState.entities, difference.entitiesDifference, progress)
    )
}

private inline fun <reified T : SimplifiedObject> interpolateObjects(
        objects: Map<Long, T>, difference: Difference<T>, progress: Float
): Map<Long, T> = objects
        .filterKeys { !difference.removed.contains(it) }
        .mapValues {
            val newObj = difference.changed[it.key]
            if (newObj == null) it.value
            else it.value.with(
                    x = interpolateValues(it.value.x, newObj.x, progress),
                    y = interpolateValues(it.value.y, newObj.y, progress)
            ) as T
        }

private fun interpolateValues(first: Float, second: Float, progress: Float): Float =
        first + (second - first) * progress

private fun SimplifiedEntity.extrapolateMovingFor(deltaTimeMillis: Long): SimplifiedEntity =
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

//TODO Projectile extrapolation

/** Extrapolates [state] for min([deltaTimeMillis], [MAX_EXTRAPOLATION_INTERVAL]) ms ahead */
private fun extrapolateSnapshot(state: SimplifiedState, deltaTimeMillis: Long): SimplifiedState {
    if (deltaTimeMillis < 0) return state
    val delta = min(deltaTimeMillis, MAX_EXTRAPOLATION_INTERVAL)
    return SimplifiedState(
            buildings = state.buildings,
            entities = state.entities.mapValues { it.value.extrapolateMovingFor(delta) }
    )
}