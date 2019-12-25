package com.mirage.view.utils

import com.mirage.core.TestSamples
import com.mirage.core.game.objects.simplified.SimplifiedObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DepthSortKtTest {

    @Test
    fun testMinorDepthSort() {
        /*
        Строение 1x1 с центром в точке (5,5) и строение 1x1 с центром в точке (4,6) - второе отрисовывается раньше
         */
        val obj1 = TestSamples.TEST_BUILDING.with(
                template = "1",
                x = 5f,
                y = 5f,
                width = 1f,
                height = 1f
        )
        val obj2 = TestSamples.TEST_BUILDING.with(
                template = "2",
                x = 4f,
                y = 6f,
                width = 1f,
                height = 1f
        )
        val objs = mutableListOf<Pair<Long, SimplifiedObject>>(0L to obj1, 1L to obj2)
        depthSort(objs)
        assertEquals(1, objs[0].first)
        assertEquals(obj2, objs[0].second)
        assertEquals(0, objs[1].first)
        assertEquals(obj1, objs[1].second)
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
        val smallBuilding = TestSamples.TEST_BUILDING.with(
                width = 0.5f,
                height = 0.5f
        )
        val player = TestSamples.TEST_ENTITY.with(
                width = 0.25f,
                height = 0.25f
        )
        val bigBuilding = TestSamples.TEST_BUILDING.with(
                width = 2.0f,
                height = 1.0f
        )
        val obj0 = smallBuilding.with(
                template = "0",
                x = 1.5f,
                y = 2.5f
        )
        val obj1 = player.with(
                template = "1",
                x = 2.5f,
                y = 2.5f
        )
        val obj2 = smallBuilding.with(
                template = "2",
                x = 0.5f,
                y = 1.5f
        )
        val obj3 = player.with(
                template = "3",
                x = 1.5f,
                y = 1.5f
        )
        val obj4 = bigBuilding.with(
                template = "4",
                x = 2.0f,
                y = 1.5f
        )
        val obj5 = smallBuilding.with(
                template = "5",
                x = 3.5f,
                y = 1.5f
        )
        val obj6 = smallBuilding.with(
                template = "6",
                x = 1.5f,
                y = 0.5f
        )
        val obj7 = player.with(
                template = "7",
                x = 2.5f,
                y = 0.5f
        )

        val unsortedObjs = listOf(
                        0L to obj5,
                        1L to obj2,
                        2L to obj0,
                        3L to obj7,
                        4L to obj6,
                        5L to obj3,
                        6L to obj1,
                        7L to obj4
        )

        //Проверяем, что compare работает корректно

        val expectedObjsList = listOf(obj0, obj1, obj2, obj3, obj4, obj5, obj6, obj7)

        for (i in 0 until 8) {
            for (j in i + 1 until 8) {
                assert(compare(expectedObjsList[i], expectedObjsList[j]) <= 0)
            }
        }

        //Проверяем, что полученный порядок является корректным с помощью compare
        val sorted = unsortedObjs.toMutableList()
        depthSort(sorted)

        for (i in 0 until 8) {
            for (j in i + 1 until 8) {
                assert(compare(sorted[i].second, sorted[j].second) <= 0)
            }
        }
    }
}