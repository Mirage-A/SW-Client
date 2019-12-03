package com.mirage.gamelogic

import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.maps.SceneLoader
import com.mirage.utils.game.states.SimplifiedState
import com.mirage.utils.messaging.GameStateUpdateMessage
import com.mirage.utils.messaging.ServerMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import kotlin.collections.ArrayList

internal class GameLogicImplTest{

    @Test
    fun testStart() {
        val state: AtomicReference<SimplifiedState?> = AtomicReference(null)
        val logic = GameLogicImpl("micro-test", {}, {objs, _ ->
            state.compareAndSet(null, objs)
        })
        logic.startLogic()
        logic.stopLogic()
        assertEquals(SceneLoader.loadInitialState("micro-test"), state.get())
    }

    @Test
    fun testNewPlayer() {
        val states = ArrayList<SimplifiedState>()
        val logic = GameLogicImpl("micro-test", {}, {objs, _ ->
            synchronized(states) {
                states.add(objs)
            }
        })
        logic.startLogic()
        Thread.sleep(50L)
        val id = AtomicLong()
        logic.addNewPlayer {
            id.set(it)
        }
        Thread.sleep(150L)
        logic.stopLogic()
        Thread.sleep(100L)
        synchronized(states) {
            assert(states.size >= 3)
            assertEquals(Long.MIN_VALUE + 1, id.get())
            assertEquals(1, states[0].entities.size)
            assertEquals(1, states[1].entities.size)
            assertEquals(2, states[2].entities.size)
        }
    }

    @Test
    fun testMinorStateUpdate() {
        val lock = Any()
        val states = ArrayList<SimplifiedState>()
        val messages = ArrayList<ServerMessage>()
        val logic = GameLogicImpl("moving-micro-test", {
            synchronized(lock) {
                messages.add(it)
            }
        }, {objs, _ ->
            synchronized(lock) {
                states.add(objs)
            }
        })
        logic.startLogic()
        Thread.sleep(150L)
        logic.stopLogic()
        Thread.sleep(100L)
        synchronized(lock) {
            assertEquals(3, states.size)
            val firstState = states[0]
            val secondState = states[1]
            val thirdState = states[2]
            println(firstState)
            println(secondState)
            println(thirdState)
            assert(firstState.entities[Long.MIN_VALUE]!!.position near Point(0.5f, 0.5f))
            assert(secondState.entities[Long.MIN_VALUE]!!.position near Point(0.5f, 0.5f))
            assert(thirdState.entities[Long.MIN_VALUE]!!.position near Point(1.5f, 0.5f))

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