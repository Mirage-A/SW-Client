package com.mirage.utils.maps

import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SceneLoaderTest {

    @Test
    fun templateMatchingTest() {
        val reader = """
            {
                "template": "main-gate",
                "name": "main-gate",
                "x": 0.4,
                "y": 0.7,
                "scripts": {
                    "onDestroy": "kill-them-all"
                }
            }
        """.trimIndent().reader()
        val difference : BuildingDifference = Gson().fromJson<BuildingDifference>(reader, BuildingDifference::class.java)
        println(difference)
        val template = SceneLoader.loadBuildingTemplate(difference.template!!)
        println(template)
        val result = difference.let {
            Building(name = it.name ?: template.name,
                    template = it.template,
                    x = it.x ?: template.x,
                    y = it.y ?: template.y,
                    width = it.width ?: template.width,
                    height = it.height ?: template.height,
                    isRigid = it.isRigid ?: template.isRigid,
                    speed = it.speed ?: template.speed,
                    moveDirection = it.moveDirection ?: template.moveDirection,
                    isMoving = it.isMoving ?: template.isMoving,
                    scripts = it.scripts ?: template.scripts
            )
        }
        println(result)
        assertEquals(result, Building(
                name = "main-gate",
                template = "main-gate",
                x = 0.4f,
                y = 0.7f,
                width = 8f,
                height = 3f,
                isRigid = false,
                speed = 0f,
                moveDirection = null,
                isMoving = false,
                scripts = mapOf("onDestroy" to "kill-them-all")
        ))
    }

    @Test
    fun listMatchingTest() {
        val reader = """
            {
            "buildingDifferences": [
            {
                "template": "main-gate",
                "name": "main-gate",
                "x": 0.4,
                "y": 0.7,
                "scripts": {
                    "onDestroy": "kill-them-all"
                }
            }
            ]
            }
        """.trimIndent().reader()
        val list = Gson().fromJson<NullableBuildingsList>(reader, NullableBuildingsList::class.java)
        println(list)
    }

    @Test
    fun loadSceneTest() {
        val mapReader = """
            {
                "width": 2,
                "height": 2,
                "tileSetName": "city",
                "tiles": [
                 1,2,3,4
                ],
                "collisions": [
                  1,2,3,1
                ]
            }
        """.trimIndent().reader()
        val buildingsReader = """
            {
            "buildingDifferences": [
                {
                    "template": "main-gate",
                    "name": "main-gate",
                    "x": 0.4,
                    "y": 0.7
                },
                {
                    "template": "wall",
                    "x": 0.0,
                    "y": 2.5
                }
            ]
            }
        """.trimIndent().reader()
        val entitiesReader = """
            {
            "entityDifferences": [
                {
                    "template": "spawn-point",
                    "x": 16.0,
                    "y": 29.875,
                    "moveDirection": "UP_RIGHT",
                    "isRigid": true,
                    "scripts": {
                        "onTileEntered": "gate-door"
                    }
                }
            ]
            }
        """.trimIndent().reader()
        //val gson = Gson()
        //val list = gson.fromJson<List<BuildingDifference>>(buildingsReader, ArrayList::class.java)
        //println(list)
        //println(list[0])
        //println(list[0]::class.java)
        val (map, objects) = SceneLoader.loadScene(mapReader, buildingsReader, entitiesReader)
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
        assertEquals(objs[2].key, Long.MIN_VALUE + 2L)
        assertEquals(Building(
                name = "main-gate",
                template = "main-gate",
                x = 0.4f,
                y = 0.7f,
                width = 8f,
                height = 3f,
                isRigid = false,
                speed = 0f,
                moveDirection = null,
                isMoving = false,
                scripts = null
        ), objs[0].value)
        assertEquals(Building(
                name = "wall",
                template = "wall",
                x = 0f,
                y = 2.5f,
                width = 4f,
                height = 3f,
                isRigid = true,
                speed = 0f,
                moveDirection = null,
                isMoving = false,
                scripts = null
        ), objs[1].value)
        assertEquals(Entity(
                name = "spawn-point",
                template = "spawn-point",
                x = 16f,
                y = 29.875f,
                width = 0.25f,
                height = 0.25f,
                isRigid = true,
                isMoving = false,
                scripts = mapOf("onTileEntered" to "gate-door"),
                speed = 2.8f,
                moveDirection = "UP_RIGHT"
        ), objs[2].value)
    }

    @Test
    fun loadBuildingByNameTemplateTest() {
        val obj = SceneLoader.loadBuildingTemplate("main-gate")
        assertEquals(Building(
                name = "main-gate",
                template = "main-gate",
                x = 0f,
                y = 0f,
                width = 8.0f,
                height = 3.0f,
                isRigid = false,
                speed = 0f,
                moveDirection = null,
                isMoving = false,
                scripts = null
        ), obj)
    }

    @Test
    fun loadEntityByNameTemplateTest() {
        val obj = SceneLoader.loadEntityTemplate("spawn-point")
        assertEquals(Entity(
                name = "spawn-point",
                template = "spawn-point",
                x = 0f,
                y = 0f,
                width = 0.25f,
                height = 0.25f,
                isRigid = false,
                isMoving = false,
                scripts = null,
                speed = 2.8f,
                moveDirection = "UP_RIGHT"
        ), obj)
    }

    @Test
    fun loadBuildingTemplateTest() {
        val json = """
            {
                "template": "main-gate",
                "width": 8.0,
                "height": 3.0,
                "isRigid": false
            }
        """.trimIndent()
        val obj = SceneLoader.loadBuildingTemplate(json.reader())
        assertEquals(Building(
                name = null,
                template = "main-gate",
                x = 0f,
                y = 0f,
                width = 8.0f,
                height = 3.0f,
                isRigid = false,
                speed = 0f,
                moveDirection = null,
                isMoving = false,
                scripts = null
        ), obj)
    }

    @Test
    fun loadEntityTemplateTest() {
        val json = """
            {
                "template": "spawn-point",
                "width": 0.25,
                "height": 0.25,
                "moveDirection": "UP_RIGHT",
                "speed": 2.8,
                "isRigid": false,
                "layer": 0
            }
        """.trimIndent()
        val obj = SceneLoader.loadEntityTemplate(json.reader())
        assertEquals(Entity(
                name = null,
                template = "spawn-point",
                x = 0f,
                y = 0f,
                width = 0.25f,
                height = 0.25f,
                isRigid = false,
                isMoving = false,
                scripts = null,
                speed = 2.8f,
                moveDirection = "UP_RIGHT"
        ), obj)
    }
}