package com.mirage.utils.messaging

import org.junit.jupiter.api.Test

internal class ServerMessageTest {

    @Test
    fun testSerializing() {
        //TODO Сериализация сообщений пока что не реализована, она нужна будет только для сетевой игры.
        /*
        fun testSeri(msg: ServerMessage) {
            val seri = msg.serialize()
            println(msg)
            println(seri)
            val deseri = ServerMessage.deserialize(seri)
            println(deseri)
            assertEquals(msg, deseri)
            println()
        }

        val obj = GameObject(
                name = "wall",
                template = "wtf",
                type = GameObject.Type.BUILDING,
                x = 2f,
                y = 5f,
                width = 2f,
                height = 6f,
                isRigid = true,
                speed = 5f,
                moveDirection = GameObject.MoveDirection.UP,
                isMoving = true,
                transparencyRange = 4f,
                state = "WTF")

        val origin = GameObjects(
                mapOf(
                        Long.MIN_VALUE to obj,
                        (Long.MIN_VALUE + 1) to obj,
                        (Long.MIN_VALUE + 2) to obj,
                        (Long.MIN_VALUE + 3) to obj,
                        (Long.MIN_VALUE + 4) to obj,
                        (Long.MIN_VALUE + 5) to obj
                ),
                Long.MIN_VALUE + 6
        )

        testSeri(MapChangeMessage("anvil", origin, 0L))
        //TODO

         */
    }
}