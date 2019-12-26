package com.mirage.logic

import com.mirage.core.datastructures.Point
import com.mirage.core.extensions.PlayerCreationRequest
import com.mirage.core.extensions.QuestProgress
import com.mirage.core.game.maps.SceneLoader
import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.states.SimplifiedState
import com.mirage.core.messaging.GameStateUpdateMessage
import com.mirage.core.messaging.InitialGameStateMessage
import com.mirage.core.messaging.ServerMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong

internal class GameLogicImplTest{

    @Test
    fun testStart() {
        val logic = GameLogicImpl("micro-test")
        logic.startLogic()
        logic.addNewPlayer(PlayerCreationRequest("", QuestProgress(), Equipment()) {})
        logic.stopLogic()
        Thread.sleep(250L)
        assertEquals(
                ExtendedSceneLoader("micro-test").loadInitialState(),
                (logic.serverMessages.peek().second as InitialGameStateMessage).initialState
        )
    }

    @Test
    fun testNewPlayer() {
        val states = CopyOnWriteArrayList<SimplifiedState>()
        val logic = GameLogicImpl("micro-test")
        logic.startLogic()
        Thread.sleep(50L)
        val id = AtomicLong()
        val request = PlayerCreationRequest("", QuestProgress(), Equipment()) { id.set(it) }
        logic.addNewPlayer(request)
        Thread.sleep(150L)
        logic.stopLogic()
        Thread.sleep(100L)
        logic.serverMessages.forEach {
            val state = it.second
            if (state is InitialGameStateMessage) {
                states.add(state.initialState)
            }
            if (state is GameStateUpdateMessage) {
                states.add(state.diff.projectOn(states.last()))
            }
        }
        assert(states.size >= 3)
        assertEquals(1, id.get())
        assertEquals(1, states[0].entities.size)
        assert(states[1].entities.size in 1..2)
        println(states[1].entities[0L])
        assertEquals(2, states[2].entities.size)

    }

    @Test
    fun testMinorStateUpdate() {
        val lock = Any()
        val states = ArrayList<SimplifiedState>()
        val messages = ArrayList<ServerMessage>()
        val logic = GameLogicImpl("moving-micro-test")
        logic.startLogic()
        Thread.sleep(150L)
        logic.stopLogic()
        Thread.sleep(100L)
        logic.serverMessages.forEach {
            val state = it.second
            if (state is InitialGameStateMessage) {
                states.add(state.initialState)
            }
            else if (state is GameStateUpdateMessage) {
                if (states.isNotEmpty()) {
                    states.add(state.diff.projectOn(states.last()))
                    messages.add(state)
                }
            }
        }
        synchronized(lock) {
            assertEquals(3, states.size)
            val firstState = states[0]
            val secondState = states[1]
            val thirdState = states[2]
            println(firstState)
            println(secondState)
            println(thirdState)
            assert(firstState.entities[0L]!!.position near Point(0.5f, 0.5f))
            assert(secondState.entities[0L]!!.position near Point(0.5f, 0.5f))
            assert(thirdState.entities[0L]!!.position near Point(1.5f, 0.5f))

            assertEquals(2, messages.size)
            println(messages)
            val firstDiff = (messages[0] as GameStateUpdateMessage).diff
            val secondDiff = (messages[1] as GameStateUpdateMessage).diff
            assertEquals(secondState, firstDiff.projectOn(firstState))
            assertEquals(thirdState, secondDiff.projectOn(secondState))
        }

    }

}