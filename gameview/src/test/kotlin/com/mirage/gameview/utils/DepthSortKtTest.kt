package com.mirage.gameview.utils

import com.mirage.utils.TestSamples
import com.mirage.utils.game.oldobjects.GameObject
import com.mirage.utils.game.oldobjects.GameObjects
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DepthSortKtTest {

    @Test
    fun testMinorDepthSort() {
        /*
        Строение 1x1 с центром в точке (5,5) и строение 1x1 с центром в точке (4,6) - второе отрисовывается раньше
         */
        val obj1 = TestSamples.TEST_GAME_OBJECT.with(
                name = "1",
                x = 5f,
                y = 5f,
                width = 1f,
                height = 1f,
                type = GameObject.Type.BUILDING
        )
        val obj2 = TestSamples.TEST_GAME_OBJECT.with(
                name = "2",
                x = 4f,
                y = 6f,
                width = 1f,
                height = 1f,
                type = GameObject.Type.BUILDING
        )
        val objs = GameObjects(mapOf(0L to obj1, 1L to obj2), 2L)
        val res = depthSort(objs)
        assertEquals(1, res[0].key)
        assertEquals(obj2, res[0].value)
        assertEquals(0, res[1].key)
        assertEquals(obj1, res[1].value)
    }

    @Test
    fun testMajorDepthSort() {
        /*
        Ожидаемый порядок:
        0 - Небольшое строение 0.5x0.5 с координатами (1.5, 2.5)
        1 - Персонаж 0.25x0.25 с координатами (2.5, 2.5)
        2 - Небольшое строение 0.5x0.5 с координатами (0.5, 1.5)
        3 - Персонаж 0.25x0.25 с координатами (1.5, 1.5)
        4 - Большое строение 2x1 с координатами (2, 1.5)
        5 - Небольшое строение 0.5x0.5 с координатами (3.5, 1.5)
        6 - Небольшое строение 0.5x0.5 с координатами (1.5, 0.5)
        7 - Персонаж 0.25x0.25 с координатами (2.5, 0.5)
         */
        val smallBuilding = TestSamples.TEST_GAME_OBJECT.with(
                width = 0.5f,
                height = 0.5f,
                type = GameObject.Type.BUILDING
        )
        val player = TestSamples.TEST_GAME_OBJECT.with(
                width = 0.25f,
                height = 0.25f,
                type = GameObject.Type.ENTITY
        )
        val bigBuilding = TestSamples.TEST_GAME_OBJECT.with(
                width = 2.0f,
                height = 1.0f,
                type = GameObject.Type.BUILDING
        )
        val obj0 = smallBuilding.with(
                name = "0",
                x = 1.5f,
                y = 2.5f
        )
        val obj1 = player.with(
                name = "1",
                x = 2.5f,
                y = 2.5f
        )
        val obj2 = smallBuilding.with(
                name = "2",
                x = 0.5f,
                y = 1.5f
        )
        val obj3 = player.with(
                name = "3",
                x = 1.5f,
                y = 1.5f
        )
        val obj4 = bigBuilding.with(
                name = "4",
                x = 2.0f,
                y = 1.5f
        )
        val obj5 = smallBuilding.with(
                name = "5",
                x = 3.5f,
                y = 1.5f
        )
        val obj6 = smallBuilding.with(
                name = "6",
                x = 1.5f,
                y = 0.5f
        )
        val obj7 = player.with(
                name = "7",
                x = 2.5f,
                y = 0.5f
        )

        val unsortedObjs = GameObjects(
                hashMapOf(
                        0L to obj5,
                        1L to obj2,
                        2L to obj0,
                        3L to obj7,
                        4L to obj6,
                        5L to obj3,
                        6L to obj1,
                        7L to obj4
                ),8L
        )

        //Проверяем, что compare работает корректно

        val expectedObjsList = listOf(obj0, obj1, obj2, obj3, obj4, obj5, obj6, obj7)

        for (i in 0 until 8) {
            for (j in i + 1 until 8) {
                assert(compare(expectedObjsList[i], expectedObjsList[j]) <= 0)
            }
        }

        //Проверяем, что полученный порядок является корректным с помощью compare

        val sorted = depthSort(unsortedObjs).map { it.value }

        for (i in 0 until 8) {
            for (j in i + 1 until 8) {
                assert(compare(sorted[i], sorted[j]) <= 0)
            }
        }
    }
}