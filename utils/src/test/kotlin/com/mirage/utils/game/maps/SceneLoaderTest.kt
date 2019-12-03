package com.mirage.utils.game.maps

import com.google.gson.Gson
import com.mirage.utils.game.objects.properties.MoveDirection
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SceneLoaderTest {
/*
    @Test
    fun testJSON() {

            val test1 = """            
                {
                "x": 0.0,
                "objects": [
    {
      "template": "wall",
      "x": 0.6,
      "y": 1.1
    },
    {
      "template": "spawn-point",
      "x": 1.0,
      "y": 1.6,
      "moveDirection": "UP_RIGHT"
    }
  ]}"""
            val testy1 = Gson().fromJson<SceneLoader.NullableObjectsList>(test1.reader(), SceneLoader.NullableObjectsList::class.java)
            println(testy1)
            println(testy1.objects)

    }

    @Test
    fun templateMatchingTest() {
        val reader = """
            {
                "template": "test-building",
                "name": "main-gate",
                "x": 0.4,
                "y": 0.7,
                "transparencyRange": 5.0
            }
        """.trimIndent().reader()
        val difference : SceneLoader.NullableGameObject = Gson().fromJson<SceneLoader.NullableGameObject>(reader, SceneLoader.NullableGameObject::class.java)
        println(difference)
        val template = SceneLoader.loadTemplate(difference.template!!)
        println(template)
        val result = difference.let {
            GameObject(name = it.name ?: template.name ?: "",
                    template = it.template ?: template.template ?: "",
                    type = GameObject.Type.fromString(it.type ?: template.type.toString()),
                    x = it.x ?: template.x ?: 0f,
                    y = it.y ?: template.y ?: 0f,
                    width = it.width ?: template.width ?: 0f,
                    height = it.height ?: template.height ?: 0f,
                    isRigid = it.isRigid ?: template.isRigid ?: false,
                    speed = it.speed ?: template.speed ?: 0f,
                    moveDirection = MoveDirection.fromString(it.moveDirection ?: template.moveDirection.toString()),
                    isMoving = it.isMoving ?: template.isMoving ?: false,
                    transparencyRange = it.transparencyRange ?: template.transparencyRange ?: 0f,
                    state = it.state ?: template.state ?: "",
                    action = it.action ?: template.action ?: "idle"
            )
        }
        println(result)
        assertEquals(result, GameObject(
                name = "main-gate",
                template = "test-building",
                type = GameObject.Type.BUILDING,
                x = 0.4f,
                y = 0.7f,
                width = 6f,
                height = 0f,
                isRigid = false,
                speed = 0f,
                moveDirection = MoveDirection.DOWN,
                isMoving = false,
                transparencyRange = 5f,
                state = "",
                action = "idle"
        ))
    }

    @Test
    fun listMatchingTest() {
        val reader = """
            {
            "buildings": [
            {
                "template": "test-building",
                "name": "main-gate",
                "x": 0.4,
                "y": 0.7
            }
            ]
            }
        """.trimIndent().reader()
        val list = Gson().fromJson<SceneLoader.NullableObjectsList>(reader, SceneLoader.NullableObjectsList::class.java)
        println(list)
    }

    @Test
    fun loadSceneTest() {
        val mapReader = """
            {
                "width": 2,
                "height": 2,
                "tileSetName": "city",
                "objects": [
                {
                    "template": "test-building",
                    "name": "main-gate",
                    "x": 0.4,
                    "y": 0.7
                },
                {
                    "template": "test-entity",
                    "x": 0.0,
                    "y": 2.5,
                    "transparencyRange": 6.0,
                    "moveDirection": "UP_RIGHT"
                }
                ],
                "tiles": [
                 1,2,3,4
                ],
                "collisions": [
                  1,2,3,1
                ]
            }
        """.trimIndent().reader()
        val (map, objects) = SceneLoader.loadScene(mapReader)
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

        val objs = objects.toList()
        assertEquals(objs[0].key, Long.MIN_VALUE)
        assertEquals(objs[1].key, Long.MIN_VALUE + 1L)
        assertEquals(GameObject(
                name = "main-gate",
                template = "test-building",
                type = GameObject.Type.BUILDING,
                x = 0.4f,
                y = 0.7f,
                width = 6f,
                height = 0f,
                isRigid = false,
                speed = 0f,
                moveDirection = MoveDirection.DOWN,
                isMoving = false,
                transparencyRange = 0f,
                state = "",
                action = "idle"
        ), objs[0].value)
        assertEquals(GameObject(
                name = "spawn-point",
                template = "test-entity",
                type = GameObject.Type.ENTITY,
                x = 0f,
                y = 2.5f,
                width = 0.25f,
                height = 0.25f,
                isRigid = false,
                speed = 2.8f,
                moveDirection = MoveDirection.UP_RIGHT,
                isMoving = false,
                transparencyRange = 6f,
                state = "",
                action = "idle"
        ), objs[1].value)
    }

    @Test
    fun loadBuildingByNameTemplateTest() {
        val obj = SceneLoader.loadTemplate("test-building")
        assertEquals(GameObject(
                name = "main-gate",
                template = "test-building",
                type = GameObject.Type.BUILDING,
                x = 0f,
                y = 0f,
                width = 6.0f,
                height = 0.0f,
                isRigid = false,
                speed = 0f,
                moveDirection = MoveDirection.DOWN,
                isMoving = false,
                transparencyRange = 0f,
                state = "",
                action = "idle"
        ), obj)
    }

    @Test
    fun loadEntityByNameTemplateTest() {
        val obj = SceneLoader.loadTemplate("test-entity")
        assertEquals(GameObject(
                name = "spawn-point",
                template = "test-entity",
                type = GameObject.Type.ENTITY,
                x = 0f,
                y = 0f,
                width = 0.25f,
                height = 0.25f,
                isRigid = false,
                isMoving = false,
                speed = 2.8f,
                moveDirection = MoveDirection.UP_RIGHT,
                transparencyRange = 0f,
                state = "",
                action = "idle"
        ), obj)
    }

    @Test
    fun loadBuildingTemplateTest() {
        val json = """
            {
                "name": "main-gate",
                "template": "test-building",
                "width": 8.0,
                "height": 3.0,
                "isRigid": false
            }
        """.trimIndent()
        val obj = SceneLoader.loadTemplate(json.reader())
        assertEquals(GameObject(
                name = "main-gate",
                template = "test-building",
                type = GameObject.Type.BUILDING,
                x = 0f,
                y = 0f,
                width = 8.0f,
                height = 3.0f,
                isRigid = false,
                speed = 0f,
                moveDirection = MoveDirection.DOWN,
                isMoving = false,
                transparencyRange = 0f,
                state = "",
                action = "idle"
        ), obj)
    }

    @Test
    fun loadEntityTemplateTest() {
        val json = """
            {
                "name": "spawn-point",
                "template": "test-entity",
                "type": "ENTITY",
                "width": 0.25,
                "height": 0.25,
                "moveDirection": "UP_RIGHT",
                "speed": 2.8,
                "isRigid": false,
                "layer": 0
            }
        """.trimIndent()
        val obj = SceneLoader.loadTemplate(json.reader())
        assertEquals(GameObject(
                name = "spawn-point",
                template = "test-entity",
                type = GameObject.Type.ENTITY,
                x = 0f,
                y = 0f,
                width = 0.25f,
                height = 0.25f,
                isRigid = false,
                isMoving = false,
                speed = 2.8f,
                moveDirection = MoveDirection.UP_RIGHT,
                transparencyRange = 0f,
                state = "",
                action = "idle"
        ), obj)
    }

    @Test
    fun testExceptions() {
        assertDoesNotThrow {
            SceneLoader.loadTemplate("{}".reader())
        }
        assertDoesNotThrow {
            SceneLoader.loadScene("".reader())
            SceneLoader.loadScene("""{
                "objects": [
    {
      "x": 0.6,
      "y": 1.1
    }
  ]
            }""".trimMargin().reader())
        }
    }*/
}