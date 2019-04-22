package com.mirage.utils.messaging

import com.mirage.utils.gameobjects.Building
import com.mirage.utils.gameobjects.GameObjects
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

        val building = Building(
                name = "wall",
                template = "wtf",
                x = 2f,
                y = 5f,
                width = 2f,
                height = 6f,
                isRigid = true,
                speed = 5f,
                moveDirection = "UP",
                isMoving = true,
                scripts = mapOf("on-enter" to "wtf", "hello" to "hi"),
                transparencyRange = 4f)

        val origin = GameObjects(
                mapOf(
                        Long.MIN_VALUE to building,
                        (Long.MIN_VALUE + 1) to building,
                        (Long.MIN_VALUE + 2) to building,
                        (Long.MIN_VALUE + 3) to building,
                        (Long.MIN_VALUE + 4) to building,
                        (Long.MIN_VALUE + 5) to building
                ),
                Long.MIN_VALUE + 6
        )

        testSeri(MapChangeMessage("anvil", origin, 0L))
        //TODO
    }
}