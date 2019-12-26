package com.mirage.connection

import com.mirage.core.datastructures.Point
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.messaging.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.atomic.AtomicReference

internal class LocalConnectionTest {

    @Test
    fun testInitialState() {
        val msgg: AtomicReference<ServerMessage?> = AtomicReference(null)
        val connection = LocalConnection("micro-test")
        connection.start()
        connection.forNewMessages { msgg.compareAndSet(null, it) }
        val msg = msgg.get() as InitialGameStateMessage
        assertEquals("micro-test", msg.mapName)
        assertEquals(1, msg.playerID)
        assertEquals(3, msg.initialState.entities.size + msg.initialState.buildings.size)
        assertEquals("wall", msg.initialState.buildings[0L]?.template)
        assertEquals("test-entity-1", msg.initialState.entities[0L]?.name)
        assertEquals("You", msg.initialState.entities[1L]?.name)
    }
/* TODO Refactor these concurrency tests
    @Test
    fun testMinorStateUpdate() {
        val messages = ArrayList<ServerMessage>()
        val connection = LocalConnection("moving-micro-test")
        connection.start()
        Thread.sleep(350L)
        connection.close()
        Thread.sleep(100L)
        connection.forNewMessages {
            synchronized(messages) {
                if (it is InitialGameStateMessage || it is GameStateUpdateMessage) {
                    messages.add(it)
                }
            }
        }
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
            assert(firstState.entities[0L]!!.position.x in 0.5f..0.7f)
            assertEquals(0.5f, firstState.entities[0L]!!.position.y)
            println(thirdState.entities[0L]!!.position)
            assert(thirdState.entities[0L]!!.position near Point(1.5f, 0.5f))
            println(fourthState.entities[0L]!!.position)
            assert(fourthState.entities[0L]!!.position near Point(2.5f, 0.5f))
            assertEquals(2, fourthState.entities.size + fourthState.buildings.size)
        }
    }


    @Test
    fun testClientMessageSending() {
        val messages = ArrayList<ServerMessage>()
        val connection = LocalConnection("moving-micro-test")
        connection.start()
        Thread.sleep(25L)
        println("Sending messages")
        connection.sendMessage(MoveDirectionClientMessage(MoveDirection.UP))
        connection.sendMessage(SetMovingClientMessage(true))
        println("Finished sending messages")
        Thread.sleep(350L)
        connection.close()
        Thread.sleep(100L)
        connection.forNewMessages {
            synchronized(messages) {
                if (it is InitialGameStateMessage || it is GameStateUpdateMessage) {
                    println("New state! ${messages.size}")
                    messages.add(it)
                }
            }
        }
        synchronized(messages) {
            assert(messages.size >= 4)
            println(messages)
            val msg1 = messages[0] as InitialGameStateMessage
            val msg2 = messages[1] as GameStateUpdateMessage
            val msg3 = messages[2] as GameStateUpdateMessage
            val msg4 = messages[3] as GameStateUpdateMessage
            println(msg1.stateCreatedTimeMillis)
            println(msg2.stateCreatedTimeMillis)
            println(msg3.stateCreatedTimeMillis)
            println(msg4.stateCreatedTimeMillis)
            val firstState = msg1.initialState
            val secondState = msg2.diff.projectOn(firstState)
            val thirdState = msg3.diff.projectOn(secondState)
            val fourthState = msg4.diff.projectOn(thirdState)
            val player1 = firstState.entities[1L]!!
            val player2 = secondState.entities[1L]!!
            val player3 = thirdState.entities[1L]!!
            val player4 = fourthState.entities[1L]!!
            println(player1)
            println(player2)
            println(player3)
            println(player4)
            assert(player1.position near Point(0.5f, 0.5f))
            assert(player2.position near Point(0.5f, 0.9f) || player2.position near Point(0.5f, 0.5f))
            assert(player3.position near Point(0.5f, 1.3f) || player3.position near Point(0.5f, 0.9f) || player3.position near Point(0.5f, 0.5f))
            assert(player4.position near Point(0.5f, 1.7f) || player4.position near Point(0.5f, 1.3f) || player4.position near Point(0.5f, 0.9f))
            assertEquals(2, fourthState.entities.size + fourthState.buildings.size)
        }
    }*/
}