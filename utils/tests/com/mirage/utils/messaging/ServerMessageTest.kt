package com.mirage.utils.messaging

import com.badlogic.gdx.maps.MapObject
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.isRigid
import com.mirage.utils.extensions.position
import com.mirage.utils.extensions.set
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ServerMessageTest {

    @Test
    fun testSerializing() {

        fun testSeri(msg: ServerMessage) {
            val seri = msg.serialize()
            println(msg)
            println(seri)
            val deseri = ServerMessage.deserialize(seri)
            println(deseri)
            assertEquals(msg, deseri)
            println()
        }

        testSeri(RemoveObjectMessage(228L))
        testSeri(PositionSnapshotMessage(PositionSnapshot(
                hashMapOf(1L to Point(0f ,0f), 228L to Point(1f, -2f)),
                hashMapOf(1L to MoveDirection.DOWN_LEFT, 23L to MoveDirection.UP_RIGHT),
                hashMapOf(1L to true, 1324L to false),
                228L)))
        testSeri(MapChangeMessage("anvil"))

        val obj = MapObject().apply {
            position = Point(5f, 10f)
            opacity = 5f
            name = "testy"
            isVisible = false
            properties["width"] = 200f
            properties["height"] = 10
            properties["rigid"] = true
            properties["script"] = "pass-tests"
        }
        println(obj.serialize())
        val newObj = deserializeMapObject(obj.serialize())
        newObj.let {
            assertEquals(5f, it.position.x)
            assertEquals(10f, it.position.y)
            assertEquals("testy", it.name)
            assertEquals(false, it.isVisible)
            assertEquals(200f, it.properties["width"])
            assertEquals(10, it.properties["height"])
            assertEquals(true, it.isRigid)
            assertEquals(true, it.properties["rigid"])
            assertEquals("pass-tests", it.properties["script"])
        }
    }
}