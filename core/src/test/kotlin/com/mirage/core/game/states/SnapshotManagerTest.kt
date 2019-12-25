package com.mirage.core.game.states

import com.mirage.core.INTERPOLATION_DELAY_MILLIS
import com.mirage.core.MAX_EXTRAPOLATION_INTERVAL
import com.mirage.core.game.objects.extended.ExtendedEntity
import com.mirage.core.game.objects.properties.MoveDirection
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SnapshotManagerTest {

    @Test
    fun test() {
        val obj = ExtendedEntity(
                template = "wtf",
                x = 0f,
                y = 0f,
                width = 0f,
                height = 0f,
                speed = 10f,
                state = "coding",
                isMoving = true,
                moveDirection = MoveDirection.UP
        )

        val mutableState = ExtendedState()

        // 0 мс, пустая сцена
        val firstState = mutableState.simplifiedDeepCopy()
        val firstDiff = StateDifference()
        val firstSnapshot = GameStateSnapshot(firstState, firstDiff, 0L)

        mutableState += obj
        mutableState += obj.with(x = 10f, y = 10f)

        // 1000 мс, добавлены 2 объекта с координатами (0, 0) и (10, 10)
        val secondState = mutableState.simplifiedDeepCopy()
        val secondDiff = StateDifference(firstState, secondState)
        val secondSnapshot = GameStateSnapshot(secondState, secondDiff, 1000L)

        mutableState.entities[0L]!!.x = 10f
        mutableState.entities[1L]!!.y = 0f

        // 2000 мс, оба объекта переместились на точку (10, 0)
        val thirdState = mutableState.simplifiedDeepCopy()
        val thirdDiff = StateDifference(secondState, thirdState)
        val thirdSnapshot = GameStateSnapshot(thirdState, thirdDiff, 2000L)

        mutableState.removeEntity(0L)
        mutableState.entities[1L]!!.x = 0f

        // 3000 мс, первый объект исчез, второй объект переместился на точку (0, 0)
        val fourthState = mutableState.simplifiedDeepCopy()
        val fourthDiff = StateDifference(thirdState, fourthState)
        val fourthSnapshot = GameStateSnapshot(fourthState, fourthDiff, 3000L)

        val snapshotManager = SnapshotManager()
        snapshotManager.addNewSnapshot(thirdSnapshot)
        snapshotManager.addNewSnapshot(firstSnapshot)
        snapshotManager.addNewSnapshot(fourthSnapshot)
        snapshotManager.addNewSnapshot(secondSnapshot)

        // На момент -500 мс данных нет
        assertDoesNotThrow {
            snapshotManager.getInterpolatedSnapshot(-500L + INTERPOLATION_DELAY_MILLIS)
        }

        // На момент 0 мс состояние совпадает с firstState
        assertEquals(firstState, snapshotManager.getInterpolatedSnapshot(0L + INTERPOLATION_DELAY_MILLIS))

        // На момент 500 мс состояние всё ещё совпадает с firstState
        assertEquals(firstState, snapshotManager.getInterpolatedSnapshot(500L + INTERPOLATION_DELAY_MILLIS))

        // На момент 1000 мс состояние совпадает с secondState
        assertEquals(secondState, snapshotManager.getInterpolatedSnapshot(1000L + INTERPOLATION_DELAY_MILLIS))

        // На момент 1500 мс объекты находятся на координатах (5, 0) и (10, 5) соответственно
        assertEquals(SimplifiedState(hashMapOf(), hashMapOf(
                0L to obj.with(x = 5f, y = 0f),
                1L to obj.with(x = 10f, y = 5f)
        )), snapshotManager.getInterpolatedSnapshot(1500L + INTERPOLATION_DELAY_MILLIS))

        // На момент 2000 мс состояние совпадает с thirdState
        assertEquals(thirdState, snapshotManager.getInterpolatedSnapshot(2000L + INTERPOLATION_DELAY_MILLIS))

        // На момент 2500 мс первый объект уже исчез, второй объект находится на координатах (5, 0)
        assertEquals(SimplifiedState(hashMapOf(), hashMapOf(
                1L to obj.with(x = 5f, y = 0f)
        )), snapshotManager.getInterpolatedSnapshot(2500L + INTERPOLATION_DELAY_MILLIS))

        // На момент 3000 мс состояние совпадает с fourthState
        assertEquals(fourthState, snapshotManager.getInterpolatedSnapshot(3000L + INTERPOLATION_DELAY_MILLIS))

        // На момент 3100 мс данных нет, поэтому происходит экстраполяция последнего известного состояния.
        // Объект двигался наверх со скоростью 10 тайлов в секунду, значит, сейчас он находится на координатах (0, 1)
        assertEquals(SimplifiedState(hashMapOf(), hashMapOf(
                1L to obj.with(x = 0f, y = 1f)
        )), snapshotManager.getInterpolatedSnapshot(3100L + INTERPOLATION_DELAY_MILLIS))

        // На момент (3000 + 100 + MAX_EXTRAPOLATION_INTERVAL) задержка данных уже превысила предел экстраполяции,
        // поэтому будет получено экстраполированное состояние на момент (3000 + MAX_EXTRAPOLATION_INTERVAL)
        assertEquals(SimplifiedState(hashMapOf(), hashMapOf(
                1L to obj.with(x = 0f, y = MAX_EXTRAPOLATION_INTERVAL * 10f / 1000f)
        )), snapshotManager.getInterpolatedSnapshot(3100L + MAX_EXTRAPOLATION_INTERVAL + INTERPOLATION_DELAY_MILLIS))
    }
}