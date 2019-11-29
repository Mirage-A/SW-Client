package com.mirage.gamelogic

import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.maps.SceneLoader
import com.mirage.utils.game.objects.GameObjects
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
        val state: AtomicReference<GameObjects?> = AtomicReference(null)
        val logic = GameLogicImpl("micro-test", {}, {objs, _ ->
            state.compareAndSet(null, objs)
        })
        logic.startLogic()
        logic.stopLogic()
        assertEquals(SceneLoader.loadScene("micro-test").second, state.get())
    }

    @Test
    fun testNewPlayer() {
        val states = ArrayList<GameObjects>()
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
            assertEquals(Long.MIN_VALUE + 2, id.get())
            assertEquals(2, states[0].objects.size)
            assertEquals(2, states[1].objects.size)
            assertEquals(3, states[2].objects.size)
        }
    }

    @Test
    fun testMinorStateUpdate() {
        val lock = Any()
        val states = ArrayList<GameObjects>()
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
            assert(firstState[Long.MIN_VALUE]!!.position near Point(0.5f, 0.5f))
            assert(secondState[Long.MIN_VALUE]!!.position near Point(0.5f, 0.5f))
            assert(thirdState[Long.MIN_VALUE]!!.position near Point(1.5f, 0.5f))

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