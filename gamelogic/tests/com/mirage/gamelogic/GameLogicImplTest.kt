package com.mirage.gamelogic

import com.mirage.utils.gameobjects.*
import com.mirage.utils.maps.SceneLoader
import com.mirage.utils.maps.StateDifference
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GameLogicImplTest{

    @Test
    fun testPausing() {
        val logic = GameLogicImpl("micro-test")
        logic.startLogic()
        for (i in 0 until 8) {
            logic.pauseLogic()
            Thread.sleep(1L)
            logic.resumeLogic()
        }
        logic.stopLogic()
    }
    @Test
    fun testStart() {
        val logic = GameLogicImpl("micro-test")
        logic.startLogic()
        Thread.sleep(1L)
        logic.pauseLogic()
        val snapshot = logic.observable.first().toBlocking().first()
        val expectedInitialState = GameObjects(
                mapOf(
                        Long.MIN_VALUE to SceneLoader.loadBuildingTemplate("wall").with(
                                x = 0.6f,
                                y = 1.1f
                        ),
                        (Long.MIN_VALUE + 1) to SceneLoader.loadEntityTemplate("spawn-point").with(
                                x = 1.0f,
                                y = 1.6f,
                                moveDirection = "UP_RIGHT"
                        )
                ),
                Long.MIN_VALUE + 2
        )
        val expectedStateDifference = StateDifference(
                objectDifferences = mapOf(Long.MIN_VALUE to BuildingDifference(), (Long.MIN_VALUE + 1) to EntityDifference())
        )
        assertEquals(expectedInitialState, snapshot.originState)
        assertEquals(expectedStateDifference, snapshot.stateDifference)
    }
}