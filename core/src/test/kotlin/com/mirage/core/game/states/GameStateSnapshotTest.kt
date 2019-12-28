package com.mirage.core.game.states

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GameStateSnapshotTest {

    @Test
    fun test() {
        val objs = SimplifiedState()
        val diff = StateDifference()
        val list = listOf(
                GameStateSnapshot(objs, diff, 0L),
                GameStateSnapshot(objs, diff, 100L),
                GameStateSnapshot(objs, diff, 50L)
        )
        val sortedList = list.sorted()
        assertEquals(0L, sortedList[0].createdTimeMillis)
        assertEquals(50L, sortedList[1].createdTimeMillis)
        assertEquals(100L, sortedList[2].createdTimeMillis)
    }
}