package com.mirage.utils.game.maps

import com.google.gson.Gson
import com.mirage.utils.TestSamples
import com.mirage.utils.extensions.fromJson
import com.mirage.utils.game.objects.extended.ExtendedBuilding
import com.mirage.utils.game.objects.extended.ExtendedEntity
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.game.states.SimplifiedState
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SceneLoaderTest {

    @Test
    fun testJSON() {

        val testState = TestSamples.TEST_TWO_GAME_OBJECTS.simplifiedDeepCopy()
        val equalState = Gson().fromJson<SimplifiedState>(Gson().toJson(testState))!!
        assertEquals(testState, equalState)

    }


    @Test
    fun loadSceneTest() {
        val mapStr = """{
                "width": 2,
                "height": 2,
                "tileSetName": "city",
                "tiles": [
                 1,2,3,4
                ],
                "collisions": [
                  1,2,3,1
                ]
            }""".trimIndent()
        val buildingsStr = """[
              {
                  "template": "test-building",
                   "name": "main-gate",
                  "x": 0.4,
                     "y": 0.7
                }
             
                ]""".trimIndent()
        val entitiesStr = """[{
                "template": "test-entity",
                  "x": 0.0,
                  "y": 2.5,
                "transparencyRange": 6.0,
                "moveDirection": "UP_RIGHT"
              }]""".trimIndent()
        val map = SceneLoader("").loadMap(mapStr.reader())
        val objects = SceneLoader("micro-test").loadInitialState(buildingsStr.reader(), entitiesStr.reader())
        assertEquals(map.width, 2)
        assertEquals(map.height, 2)
        assertEquals(map.tileSetName, "city")
        assert(map.isTileWalkable(0,0))
        assert(map.isTileShootable(0,0))
        assert(!map.isTileWalkable(1,0))
        assert(map.isTileShootable(1,0))
        assert(!map.isTileWalkable(0,1))
        assert(!map.isTileShootable(0,1))
        assert(map.isTileWalkable(1,1))
        assert(map.isTileShootable(1,1))
        assertEquals(1, map.getTileID(0, 0))
        assertEquals(2, map.getTileID(1, 0))
        assertEquals(3, map.getTileID(0, 1))
        assertEquals(4, map.getTileID(1, 1))

        assertEquals(ExtendedBuilding(
                template = "test-building",
                x = 0.4f,
                y = 0.7f,
                name = "main-gate",
                width = 6f,
                height = 0f,
                isRigid = false,
                transparencyRange = 0f,
                state = "default"
        ), objects.buildings[0L])
        assertEquals(ExtendedEntity(
                name = "test-entity-1",
                template = "test-entity",
                x = 0f,
                y = 2.5f,
                width = 0.25f,
                height = 0.25f,
                isRigid = false,
                speed = 2.8f,
                moveDirection = MoveDirection.UP_RIGHT,
                isMoving = false,
                state = "default",
                action = "idle"
        ), objects.entities[0L])
    }

    @Test
    fun loadBuildingByNameTemplateTest() {
        val obj = SceneLoader("micro-test").loadBuildingTemplate("test-building")
        assertEquals(ExtendedBuilding(
                template = "test-building",
                x = 0f,
                y = 0f,
                name = "main-gate",
                width = 6.0f,
                height = 0.0f,
                isRigid = false,
                transparencyRange = 0f,
                state = "default"
        ), obj)
    }

    @Test
    fun loadEntityByNameTemplateTest() {
        val obj = SceneLoader("moving-micro-test").loadEntityTemplate("test-entity")
        assertEquals(ExtendedEntity(
                name = "test-entity-1",
                template = "test-entity",
                x = 0f,
                y = 0f,
                width = 0.25f,
                height = 0.25f,
                isRigid = false,
                isMoving = false,
                speed = 2.8f,
                moveDirection = MoveDirection.UP_RIGHT,
                state = "default",
                action = "idle"
        ), obj)
    }


    @Test
    fun testExceptions() {
        assertDoesNotThrow {
            SceneLoader("").loadMap("".reader())
            SceneLoader("").loadMap("""{
                "objects": [
    {
      "x": 0.6,
      "y": 1.1
    }
  ]
            }""".trimMargin().reader())
        }
        assertDoesNotThrow {
            SceneLoader("").loadInitialState("".reader(), "".reader())
            SceneLoader("").loadInitialState("""{
                "objects": [
    {
      "x": 0.6,
      "y": 1.1
    }
  ]
            }""".trimMargin().reader(),
                    """[
    {
      "x": 0.6,
      "y": 1.1
    }
  ]""".trimMargin().reader())
        }
    }
}