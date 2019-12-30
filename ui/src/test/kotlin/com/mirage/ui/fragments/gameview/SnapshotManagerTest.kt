package com.mirage.ui.fragments.gameview

import com.mirage.core.game.objects.Difference
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.game.objects.SimplifiedEntity
import com.mirage.core.game.objects.SimplifiedState
import com.mirage.core.game.objects.StateDifference
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class SnapshotManagerTest {

    @Disabled
    @Test
    fun test() {
        val obj = SimplifiedEntity(
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

        // 0 мс, пустая сцена
        val firstState = SimplifiedState()
        val firstDiff = StateDifference()

        // 1000 мс, добавлены 2 объекта с координатами (0, 0) и (10, 10)
        val secondDiff = StateDifference(
                entitiesDifference = Difference(new = mapOf(0L to obj, 1L to obj.with(x = 10f, y = 10f)))
        )
        val secondState = secondDiff.projectOn(firstState)

        // 2000 мс, оба объекта переместились на точку (10, 0)
        val thirdDiff = StateDifference(
                entitiesDifference = Difference(
                        changed = mapOf(0L to secondState.entities[0L]!!.with(x = 10f), 1L to secondState.entities[1L]!!.with(y = 0f))
                )
        )
        val thirdState = thirdDiff.projectOn(secondState)

        // 3000 мс, первый объект исчез, второй объект переместился на точку (0, 0)
        val fourthDiff = StateDifference(
                entitiesDifference = Difference(
                        removed = listOf(0L),
                        changed = mapOf(1L to thirdState.entities[1L]!!.with(x = 0f))
                )
        )
        val fourthState = fourthDiff.projectOn(thirdState)

        val snapshotManager = SnapshotManager()
        snapshotManager.setInitialState(firstState, 0L)
        snapshotManager.addSnapshot(thirdDiff, 2000L)
        snapshotManager.addSnapshot(fourthDiff, 3000L)
        snapshotManager.addSnapshot(secondDiff, 1000L)

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