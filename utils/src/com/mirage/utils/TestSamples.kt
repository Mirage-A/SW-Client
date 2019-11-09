package com.mirage.utils

import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.game.objects.GameObjects

/**
 * Объекты, которые удобно использовать для тестов.
 **/
object TestSamples {
    val TEST_GAME_OBJECT = GameObject(
            name = "test",
            template = "test",
            type = GameObject.Type.BUILDING,
            x = 0f,
            y = 0f,
            width = 0f,
            height = 0f,
            isRigid = false,
            speed = 0f,
            isMoving = false,
            moveDirection = GameObject.MoveDirection.DOWN,
            transparencyRange = 0f,
            state = ""
    )

    val TEST_NO_GAME_OBJECTS = GameObjects(hashMapOf(), 0L)

    val TEST_TWO_GAME_OBJECTS = GameObjects(hashMapOf(0L to TEST_GAME_OBJECT, 1L to TEST_GAME_OBJECT), 2L)

    val TEST_SMALL_MAP = GameMap(
            4, 4, 2f, 2f, "test", 0, Array(16) { 0 }.toList(), Array(16) { 1 }.toList()
    )
}