package com.mirage.gamelogic

import com.mirage.utils.TestSamples
import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.game.states.ExtendedState
import com.mirage.utils.messaging.ServerMessage
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.ArrayList

internal class GameStateUpdateKtTest {

    @Test
    fun testNoChanges() {
        val state = TestSamples.TEST_TWO_GAME_OBJECTS
        val map = TestSamples.TEST_SMALL_MAP
        val messages = ArrayDeque<ServerMessage>()
        updateState(100L, state, map, ArrayList(), messages)
        assert(messages.isEmpty())
    }

    @Test
    fun testMoving() {
        val obj = TestSamples.TEST_ENTITY.with(
                x = 0.5f,
                y = 0.5f,
                width = 0f,
                height = 0f,
                isMoving = true,
                moveDirection = MoveDirection.UP,
                speed = 4f,
                isRigid = false
        )
        val state = ExtendedState(listOf(), listOf(obj))
        val map = TestSamples.TEST_SMALL_MAP
        val messages = ArrayDeque<ServerMessage>()
        updateState(250L, state, map, ArrayList(), messages)
        assert(Point(0.5f, 1.5f) near state.entities[Long.MIN_VALUE]!!.position)
        updateState(1000L, state, map, ArrayList(), messages)
        assert(Point(0.5f, 4f - EPS) near state.entities[Long.MIN_VALUE]!!.position)
        updateState(1500L, state, map, ArrayList(), messages)
        assert(Point(0.5f, 4f - EPS) near state.entities[Long.MIN_VALUE]!!.position)
        assert(messages.isEmpty())
    }
}