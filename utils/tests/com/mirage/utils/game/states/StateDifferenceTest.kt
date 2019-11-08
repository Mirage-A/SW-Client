package com.mirage.utils.game.states

import com.mirage.utils.extensions.treeSetOf
import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.game.objects.GameObjects
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class StateDifferenceTest {

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
        val firstState = GameObjects(hashMapOf(
                0L to obj1,
                1L to obj2,
                2L to obj1,
                3L to obj2
        ), 4L)
        val mutableState = firstState.mutableCopy()
        mutableState.remove(1L)
        mutableState.add(obj1)
        mutableState[0]!!.name = "newName"
        val expectedDiff = StateDifference(hashMapOf(4L to obj1), treeSetOf(1L), hashMapOf(0L to obj1.with(name = "newName")))
        val actualDiff = mutableState.findDifferenceWithOrigin()
        assertEquals(expectedDiff, actualDiff)
        assertEquals(mutableState.saveChanges(), actualDiff.projectOn(firstState))
    }
}