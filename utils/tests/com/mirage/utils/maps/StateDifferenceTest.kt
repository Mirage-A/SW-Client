package com.mirage.utils.maps

import com.mirage.utils.gameobjects.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class StateDifferenceTest{

    @Test
    fun testProjection() {
        val obj = Building(
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
                scripts = mapOf("on-enter" to "wtf", "hello" to "hi"))
        val diff = StateDifference(
                newObjects = listOf(obj),
                removedObjects = listOf(Long.MIN_VALUE, Long.MIN_VALUE + 2),
                objectDifferences = mapOf(
                        (Long.MIN_VALUE + 3) to BuildingDifference(
                                template = "new_wtf",
                                height = null,
                                x = 99f,
                                moveDirection = "WOW",
                                scripts = mapOf("foo" to "bar")
                        )
                ),
                newClientScripts = listOf("kill-them-all")
        )
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
        assertEquals(6, origin.toList().size)
        val new = diff.projectOn(origin)
        val objs = new.toList()
        assertEquals(5, objs.size)
        assertEquals(Long.MIN_VALUE + 1, objs[0].key)
        assertEquals(Long.MIN_VALUE + 3, objs[1].key)
        assertEquals(Long.MIN_VALUE + 4, objs[2].key)
        assertEquals(Long.MIN_VALUE + 5, objs[3].key)
        assertEquals(Long.MIN_VALUE + 6, objs[4].key)
        assertEquals(obj, objs[0].value)
        assertEquals(obj, objs[2].value)
        assertEquals(obj, objs[3].value)
        assertEquals(obj, objs[4].value)
        val expected = Building(
                name = "wall",
                template = "new_wtf",
                x = 99f,
                y = 5f,
                width = 2f,
                height = 6f,
                isRigid = true,
                speed = 5f,
                moveDirection = "WOW",
                isMoving = true,
                scripts = mapOf("foo" to "bar")
        )
        assertEquals(expected, objs[1].value)
        assertEquals(expected, obj.with(
                template = "new_wtf",
                x = 99f,
                moveDirection = "WOW",
                scripts = mapOf("foo" to "bar")
        ))
    }
}