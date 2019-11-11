package com.mirage.utils.game.objects

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GameObjectsTest{

    @Test
    fun testUpdate() {
        val building = GameObject(
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
                state = "")
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
        assertEquals(6, origin.toList().size)
        val new = origin.update(
                newObjects = mapOf(Long.MIN_VALUE + 6 to building),
                map = {id, obj ->
                    if (id == Long.MIN_VALUE || id == Long.MIN_VALUE + 2) null
                    else if (id == Long.MIN_VALUE + 3) building.with(
                            template = "new_wtf",
                            x = 99f,
                            moveDirection = GameObject.MoveDirection.DOWN
                    )
                    else obj
                }
        )
        val objs = new.toList()
        assertEquals(5, objs.size)
        assertEquals(Long.MIN_VALUE + 1, objs[0].key)
        assertEquals(Long.MIN_VALUE + 3, objs[1].key)
        assertEquals(Long.MIN_VALUE + 4, objs[2].key)
        assertEquals(Long.MIN_VALUE + 5, objs[3].key)
        assertEquals(Long.MIN_VALUE + 6, objs[4].key)
        assertEquals(building, objs[0].value)
        assertEquals(building, objs[2].value)
        assertEquals(building, objs[3].value)
        assertEquals(building, objs[4].value)
        val expected = GameObject(
                name = "wall",
                template = "new_wtf",
                type = GameObject.Type.BUILDING,
                x = 99f,
                y = 5f,
                width = 2f,
                height = 6f,
                isRigid = true,
                speed = 5f,
                moveDirection = GameObject.MoveDirection.DOWN,
                isMoving = true,
                transparencyRange = 0f,
                state = ""
        )
        assertEquals(expected, objs[1].value)
    }
}