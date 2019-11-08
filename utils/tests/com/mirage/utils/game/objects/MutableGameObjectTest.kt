package com.mirage.utils.game.objects

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MutableGameObjectTest {

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
                state = ""
        )
        assertEquals(obj.mutableCopy().saveChanges(), obj)
    }
}