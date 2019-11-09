package com.mirage.connection

import com.mirage.gamelogic.GameLogicImpl
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.treeSetOf
import com.mirage.utils.game.maps.SceneLoader
import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.game.states.StateDifference
import com.mirage.utils.messaging.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class LocalConnectionTest {

    @Test
    fun testInitialState() {
        val connection = LocalConnection("micro-test")
        connection.start()
        val msg = connection.serverMessages.toBlocking().first() as InitialGameStateMessage
        assertEquals("micro-test", msg.mapName)
        assertEquals(Long.MIN_VALUE + 2, msg.playerID)
        assertEquals(3, msg.initialObjects.objects.size)
        assertEquals("wall", msg.initialObjects[Long.MIN_VALUE]!!.name)
        assertEquals("spawn-point", msg.initialObjects[Long.MIN_VALUE + 1]!!.name)
        assertEquals("player", msg.initialObjects[Long.MIN_VALUE + 2]!!.name)
    }

    @Test
    fun testMinorStateUpdate() {
        val connection = LocalConnection("moving-micro-test")
        connection.start()
        Thread.sleep(5L)
        Thread.sleep(25L)
        val messages = ArrayList<ServerMessage>()
        connection.serverMessages.asObservable().subscribe {
            if (it is InitialGameStateMessage || it is GameStateUpdateMessage) {
                messages.add(it)
            }
        }
        Thread.sleep(125L)
        connection.close()
        Thread.sleep(100L)
        val msg1 = messages[0] as InitialGameStateMessage
        val msg2 = messages[1] as GameStateUpdateMessage
        val msg3 = messages[2] as GameStateUpdateMessage
        val msg4 = messages[3] as GameStateUpdateMessage
        val firstState = msg1.initialObjects
        val secondState = msg2.diff.projectOn(firstState)
        val thirdState = msg3.diff.projectOn(secondState)
        val fourthState = msg4.diff.projectOn(thirdState)
        assert(Point(0.5f, 0.5f) near firstState[Long.MIN_VALUE]!!.position)
        assert(thirdState[Long.MIN_VALUE]!!.position.x in 1.4f..1.6f)
        assert(thirdState[Long.MIN_VALUE]!!.position.y in 0.4f..0.6f)
        assert(fourthState[Long.MIN_VALUE]!!.position.x in 2.4f..2.6f)
        assert(fourthState[Long.MIN_VALUE]!!.position.y in 0.4f..0.6f)
        assertEquals(2, fourthState.objects.size)
    }


    @Test
    fun testClientMessageSending() {
        val connection = LocalConnection("moving-micro-test")
        connection.start()
        connection.sendMessage(MoveDirectionClientMessage(GameObject.MoveDirection.UP))
        connection.sendMessage(SetMovingClientMessage(true))
        Thread.sleep(5L)
        Thread.sleep(25L)
        val messages = ArrayList<ServerMessage>()
        connection.serverMessages.asObservable().subscribe {
            if (it is InitialGameStateMessage || it is GameStateUpdateMessage) {
                messages.add(it)
            }
        }
        Thread.sleep(125L)
        connection.close()
        Thread.sleep(100L)
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
        assert(Point(0.5f, 0.5f) near player1.position)
        assert(player3.position.x in 0.4f..0.6f)
        assert(player3.position.y in 0.74f..0.8f)
        assert(player4.position.x in 0.4f..0.6f)
        assert(player4.position.y in 0.98f..1.1f)
        assertEquals(2, fourthState.objects.size)
    }
}