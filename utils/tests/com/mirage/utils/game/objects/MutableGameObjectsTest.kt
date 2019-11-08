package com.mirage.utils.game.objects

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class MutableGameObjectsTest {

    @Test
    fun test() {
        val obj1 = GameObject(
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
                state = "coding"
        )
        val obj2 = GameObject(
                name = "gate",
                template = "ftw",
                type = GameObject.Type.ENTITY,
                x = 1f,
                y = 7f,
                width = 1f,
                height = 99f,
                isRigid = false,
                speed = 50f,
                moveDirection = GameObject.MoveDirection.DOWN_RIGHT,
                isMoving = false,
                transparencyRange = -100f,
                state = "hello!"
        )
        val emptyState = GameObjects(hashMapOf(99L to obj1, 100L to obj2), 0L)
        val mutableState = emptyState.createMutableObjectsCopy()
        mutableState.remove(99L)
        mutableState.add(obj1.mutableCopy())
        mutableState.add(obj2.mutableCopy())
        mutableState.add(obj1.mutableCopy())
        mutableState.add(obj2.mutableCopy())
        assertEquals(4L, mutableState.saveChanges().nextID)
        mutableState.remove(1L)
        mutableState.remove(4L)
        mutableState.remove(-1L)
        mutableState[0]!!.name = "bigger_wall"
        mutableState[100]!!.name = "bigger_gate"
        assertEquals(null, mutableState[1L])
        assertEquals(null, mutableState[4L])
        val objs = mutableState.saveChanges()
        assertEquals("bigger_wall", objs[0L]!!.name)
        assertEquals("wall", objs[2L]!!.name)
        assertEquals("gate", objs[3L]!!.name)
        assertEquals(null, objs[1L])
        assertEquals(4L, mutableState.saveChanges().nextID)
        val diff = mutableState.findDifferenceWithOrigin()
        assertEquals(hashMapOf(
                0L to obj1.with(name = "bigger_wall"),
                2L to obj1,
                3L to obj2), diff.newObjects)
        assertEquals(TreeSet<Long>().apply { add(99L) }, diff.removedObjects)
        assertEquals(hashMapOf(100L to obj2.with(name = "bigger_gate")), diff.changedObjects)
    }
}