package com.mirage.connection

import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.messaging.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.atomic.AtomicReference

internal class LocalConnectionTest {

    @Test
    fun testInitialState() {
        val msgg: AtomicReference<ServerMessage?> = AtomicReference(null)
        val connection = LocalConnection("micro-test") {
            msgg.compareAndSet(null, it)
        }
        connection.start()
        val msg = msgg.get() as InitialGameStateMessage
        assertEquals("micro-test", msg.mapName)
        assertEquals(Long.MIN_VALUE + 1, msg.playerID)
        assertEquals(3, msg.initialState.entities.size + msg.initialState.buildings.size)
        assertEquals("wall", msg.initialState.buildings[Long.MIN_VALUE]?.template)
        assertEquals("spawn-point", msg.initialState.entities[Long.MIN_VALUE]?.name)
        assertEquals("player", msg.initialState.entities[Long.MIN_VALUE + 1]?.name)
    }

    @Test
    fun testMinorStateUpdate() {
        val messages = ArrayList<ServerMessage>()
        val connection = LocalConnection("moving-micro-test") {
            synchronized(messages) {
                if (it is InitialGameStateMessage || it is GameStateUpdateMessage) {
                    messages.add(it)
                }
            }
        }
        connection.start()
        Thread.sleep(350L)
        connection.close()
        Thread.sleep(100L)
        synchronized(messages) {
            assert(messages.size >= 4)
            val msg1 = messages[0] as InitialGameStateMessage
            val msg2 = messages[1] as GameStateUpdateMessage
            val msg3 = messages[2] as GameStateUpdateMessage
            val msg4 = messages[3] as GameStateUpdateMessage
            val firstState = msg1.initialState
            val secondState = msg2.diff.projectOn(firstState)
            val thirdState = msg3.diff.projectOn(secondState)
            val fourthState = msg4.diff.projectOn(thirdState)
            assert(firstState.entities[Long.MIN_VALUE]!!.position.x in 0.5f..0.7f)
            assertEquals(0.5f, firstState.entities[Long.MIN_VALUE]!!.position.y)
            println(thirdState.entities[Long.MIN_VALUE]!!.position)
            assert(thirdState.entities[Long.MIN_VALUE]!!.position near Point(2.5f, 0.5f))
            println(fourthState.entities[Long.MIN_VALUE]!!.position)
            assert(fourthState.entities[Long.MIN_VALUE]!!.position near Point(3.5f, 0.5f))
            assertEquals(2, fourthState.entities.size + fourthState.buildings.size)
        }
    }


    @Test
    fun testClientMessageSending() {
        val messages = ArrayList<ServerMessage>()
        val connection: AtomicReference<Connection?> = AtomicReference(null)
        connection.set(LocalConnection("moving-micro-test") {
            synchronized(messages) {
                if (it is InitialGameStateMessage || it is GameStateUpdateMessage) {
                    println("New state! ${messages.size}")
                    messages.add(it)
                }
                if (messages.size == 2) {
                    connection.get()!!.sendMessage(MoveDirectionClientMessage(MoveDirection.UP))
                    connection.get()!!.sendMessage(SetMovingClientMessage(true))
                }
            }
        })
        connection.get()!!.start()
        Thread.sleep(350L)
        connection.get()!!.close()
        Thread.sleep(100L)
        synchronized(messages) {
            assert(messages.size >= 4)
            val msg1 = messages[0] as InitialGameStateMessage
            val msg2 = messages[1] as GameStateUpdateMessage
            val msg3 = messages[2] as GameStateUpdateMessage
            val msg4 = messages[3] as GameStateUpdateMessage
            val firstState = msg1.initialState
            val secondState = msg2.diff.projectOn(firstState)
            val thirdState = msg3.diff.projectOn(secondState)
            val fourthState = msg4.diff.projectOn(thirdState)
            val player1 = firstState.entities[Long.MIN_VALUE + 1]!!
            val player2 = secondState.entities[Long.MIN_VALUE + 1]!!
            val player3 = thirdState.entities[Long.MIN_VALUE + 1]!!
            val player4 = fourthState.entities[Long.MIN_VALUE + 1]!!
            println(player1)
            println(player2)
            println(player3)
            println(player4)
            assert(player1.position near Point(0.5f, 0.5f))
            assert(player2.position near Point(0.5f, 0.5f))
            assert(player3.position near Point(0.5f, 0.9f))
            assert(player4.position near Point(0.5f, 1.3f))
            assertEquals(2, fourthState.entities.size + fourthState.buildings.size)
        }
    }
}