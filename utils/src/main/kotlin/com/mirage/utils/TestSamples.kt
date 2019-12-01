package com.mirage.utils

import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.objects.SimplifiedBuilding
import com.mirage.utils.game.objects.SimplifiedObject

/**
 * Объекты, которые удобно использовать для тестов.
 **/
object TestSamples {
    val TEST_GAME_OBJECT = SimplifiedBuilding(
            template = "test",
            x = 0f,
            y = 0f,
            width = 0f,
            height = 0f,
            transparencyRange = 0f,
            state = ""
    )

    val TEST_NO_GAME_OBJECTS = HashMap<Long, SimplifiedObject>()

    val TEST_TWO_GAME_OBJECTS = hashMapOf(Long.MIN_VALUE to TEST_GAME_OBJECT, Long.MIN_VALUE + 1L to TEST_GAME_OBJECT)

    val TEST_SMALL_MAP = GameMap(
            4, 4, 2f, 2f, "test", 0, Array(16) { 0 }.toList(), Array(16) { 1 }.toList()
    )
}