package com.mirage.utils

import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.objects.extended.ExtendedBuilding
import com.mirage.utils.game.objects.extended.ExtendedEntity
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.game.objects.simplified.SimplifiedBuilding
import com.mirage.utils.game.states.ExtendedState

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

    val TEST_BUILDING = ExtendedBuilding(
            template = "test",
            x = 0f,
            y = 0f,
            width = 0f,
            height = 0f,
            transparencyRange = 0f,
            state = "",
            isRigid = false
    )

    val TEST_ENTITY = ExtendedEntity(
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

    val TEST_NO_GAME_OBJECTS = ExtendedState(ArrayList(), ArrayList())

    val TEST_TWO_GAME_OBJECTS = ExtendedState(listOf(TEST_BUILDING), listOf(TEST_ENTITY))

    val TEST_SMALL_MAP = GameMap(
            4, 4, 2f, 2f, "test", 0, Array(16) { 0 }.toList(), Array(16) { 1 }.toList()
    )
}