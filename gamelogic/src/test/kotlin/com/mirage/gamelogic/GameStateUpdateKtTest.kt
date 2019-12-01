package com.mirage.gamelogic

import com.mirage.utils.TestSamples
import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.oldobjects.GameObject
import com.mirage.utils.game.oldobjects.GameObjects
import org.junit.jupiter.api.Test

internal class GameStateUpdateKtTest {

    @Test
    fun testNoChanges() {
        val state = TestSamples.TEST_TWO_GAME_OBJECTS
        val map = TestSamples.TEST_SMALL_MAP
        val result = updateState(100L, state, map, ArrayList())
        //TODO assertEquals(state, result.first.saveChanges())
        assert(result.second.isEmpty())
    }

    @Test
    fun testMoving() {
        val obj = TestSamples.TEST_GAME_OBJECT.with(
                x = 0.5f,
                y = 0.5f,
                width = 0f,
                height = 0f,
                isMoving = true,
                moveDirection = GameObject.MoveDirection.UP,
                speed = 4f
        )
        val state = GameObjects(mapOf(0L to obj), 1L)
        val map = TestSamples.TEST_SMALL_MAP
        val firstResult = updateState(250L, state, map, ArrayList())
        println(firstResult.first[0]!!.position)
        assert(Point(0.5f, 1.5f) near firstResult.first[0]!!.position)
        assert(firstResult.second.isEmpty())
        val secondResult = updateState(1000L, state, map, ArrayList())
        assert(Point(0.5f, 4f - EPS) near secondResult.first[0]!!.position)
        assert(secondResult.second.isEmpty())
        val thirdResult = updateState(1500L, state, map, ArrayList())
        assert(Point(0.5f, 4f - EPS) near thirdResult.first[0]!!.position)
        assert(thirdResult.second.isEmpty())
    }
}