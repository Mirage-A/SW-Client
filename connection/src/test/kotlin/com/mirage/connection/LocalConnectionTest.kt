package com.mirage.connection

import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.objects.GameObject
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
        assertEquals(Long.MIN_VALUE + 2, msg.playerID)
        assertEquals(3, msg.initialObjects.objects.size)
        assertEquals("wall", msg.initialObjects[Long.MIN_VALUE]!!.name)
        assertEquals("spawn-point", msg.initialObjects[Long.MIN_VALUE + 1]!!.name)
        assertEquals("player", msg.initialObjects[Long.MIN_VALUE + 2]!!.name)
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
            println(messages.size)
            println(messages)
            assertEquals(4, messages.size)
            val msg1 = messages[0] as InitialGameStateMessage
            val msg2 = messages[1] as GameStateUpdateMessage
            val msg3 = messages[2] as GameStateUpdateMessage
            val msg4 = messages[3] as GameStateUpdateMessage
            val firstState = msg1.initialObjects
            val secondState = msg2.diff.projectOn(firstState)
            val thirdState = msg3.diff.projectOn(secondState)
            val fourthState = msg4.diff.projectOn(thirdState)
            println(firstState)
            println(secondState)
            println(thirdState)
            println(fourthState)
            assert(Point(0.5f, 0.5f) near firstState[Long.MIN_VALUE]!!.position)
            println(thirdState[Long.MIN_VALUE]!!.position)
            assert(thirdState[Long.MIN_VALUE]!!.position near Point(2.5f, 0.5f))
            println(fourthState[Long.MIN_VALUE]!!.position)
            assert(fourthState[Long.MIN_VALUE]!!.position near Point(3.5f, 0.5f))
            assertEquals(2, fourthState.objects.size)
        }
    }


    @Test
    fun testClientMessageSending() {
        val messages = ArrayList<ServerMessage>()
        val connection = LocalConnection("moving-micro-test") {
            synchronized(messages) {
                if (it is InitialGameStateMessage || it is GameStateUpdateMessage) {
                    messages.add(it)
                }
            }
        }
        connection.start()
        connection.sendMessage(MoveDirectionClientMessage(GameObject.MoveDirection.UP))
        connection.sendMessage(SetMovingClientMessage(true))
        Thread.sleep(350L)
        connection.close()
        Thread.sleep(100L)
        synchronized(messages) {
            println(messages.size)
            println(messages)
            assert(messages.size >= 4)
            val msg1 = messages[0] as InitialGameStateMessage
            val msg2 = messages[1] as GameStateUpdateMessage
            val msg3 = messages[2] as GameStateUpdateMessage
            val msg4 = messages[3] as GameStateUpdateMessage
            val firstState = msg1.initialObjects
            val secondState = msg2.diff.projectOn(firstState)
            val thirdState = msg3.diff.projectOn(secondState)
            val fourthState = msg4.diff.projectOn(thirdState)
            val player1 = firstState[Long.MIN_VALUE + 1]!!
            val player2 = secondState[Long.MIN_VALUE + 1]!!
            val player3 = thirdState[Long.MIN_VALUE + 1]!!
            val player4 = fourthState[Long.MIN_VALUE + 1]!!
            println(player1)
            println(player2)
            println(player3)
            println(player4)
            assert(player1.position near Point(0.5f, 0.5f))
            assert(player2.position near Point(0.5f, 0.9f))
            assert(player3.position near Point(0.5f, 1.3f))
            assert(player4.position near Point(0.5f, 1.7f))
            assertEquals(2, fourthState.objects.size)
        }
    }
}