package com.mirage.utils.game.objects

import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GameObjectTest {

    @Test
    fun test() {
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
                transparencyRange = 0f,
                state = "",
                action = ""
        )
        assertEquals(obj.mutableCopy().saveChanges(), obj)
        assertEquals(obj.move(500L), obj.with(y = 7.5f))
        assertEquals(obj.rectangle, Rectangle(2f, 5f, 2f, 6f))
        assertEquals(obj.position, Point(2f, 5f))
    }
}