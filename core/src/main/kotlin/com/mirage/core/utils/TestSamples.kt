package com.mirage.core.utils

import com.mirage.core.game.maps.GameMap
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.game.objects.SimplifiedBuilding
import com.mirage.core.game.objects.SimplifiedEntity
import com.mirage.core.game.objects.SimplifiedState

//TODO Remove this class
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

    val TEST_BUILDING = SimplifiedBuilding(
            template = "test",
            x = 0f,
            y = 0f,
            width = 0f,
            height = 0f,
            transparencyRange = 0f,
            state = ""
    )

    val TEST_ENTITY = SimplifiedEntity(
            template = "player",
            x = 0f,
            y = 0f,
            name = "admin",
            width = 0.25f,
            height = 0.25f,
            speed = 2.8f,
            moveDirection = MoveDirection.UP,
            isMoving = false,
            state = "default",
            action = "idle"
    )

    val TEST_NO_GAME_OBJECTS = SimplifiedState(ArrayList(), ArrayList())

    val TEST_TWO_GAME_OBJECTS = SimplifiedState(listOf(TEST_BUILDING), listOf(TEST_ENTITY))

    val TEST_SMALL_MAP = GameMap(
            null, null, 4, 4, 2f, 2f, "test", 0, Array(16) { 0 }.toList(), Array(16) { 1 }.toList()
    )
}