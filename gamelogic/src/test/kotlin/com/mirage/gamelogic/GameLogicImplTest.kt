package com.mirage.gamelogic

import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.maps.SceneLoader
import com.mirage.utils.game.states.SimplifiedState
import com.mirage.utils.messaging.GameStateUpdateMessage
import com.mirage.utils.messaging.InitialGameStateMessage
import com.mirage.utils.messaging.ServerMessage
import com.mirage.utils.messaging.serializeClientMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import kotlin.collections.ArrayList

internal class GameLogicImplTest{

    @Test
    fun testStart() {
        val logic = GameLogicImpl("micro-test")
        logic.startLogic()
        logic.addNewPlayer {  }
        logic.stopLogic()
        Thread.sleep(250L)
        assertEquals(
                SceneLoader.loadInitialState("micro-test"),
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
        logic.addNewPlayer {
            id.set(it)
        }
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
        assertEquals(1, states[1].entities.size)
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


    @Test
    fun testJavaUtilTimer() {
        val timer = Timer(false)
        val counter = AtomicInteger(0)
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                counter.incrementAndGet()
            }
        }
        timer.scheduleAtFixedRate(task, 0L, 10L)
        Thread.sleep(95L)
        timer.cancel()
        assertEquals(10, counter.get())
    }
}