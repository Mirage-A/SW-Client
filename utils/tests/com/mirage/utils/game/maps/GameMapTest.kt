package com.mirage.utils.game.maps

import com.google.gson.Gson
import com.mirage.utils.game.objects.GameObject
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GameMapTest {

    @Test
    fun testGSONSerialization() {
        val obj = GameObject(
                "test_name",
                "default_template",
                GameObject.Type.BUILDING,
                0f,
                0f,
                0f,
                0f,
                false,
                0f,
                GameObject.MoveDirection.DOWN,
                false,
                5f,
                 "test_state",
                "test_action"
        )
        val gson = Gson()
        val str = gson.toJson(obj)
        val newObj = gson.fromJson<GameObject>(str, GameObject::class.java)
        assertEquals(obj, newObj)

        val mapStr = """{
  "width": 3,
  "height": 2,
  "tileSetName": "city",
  "tiles": [
    5,6,7,8,9,10
  ],
  "collisions": [
    1,2,3,2,1,1]
}"""
        val map = gson.fromJson<GameMap>(mapStr, GameMap::class.java)
        assertEquals(3, map.width)
        assertEquals(2, map.height)
        assertEquals("city", map.tileSetName)
        assertEquals(5, map.getTileID(0, 0))
        assertEquals(6, map.getTileID(1, 0))
        assertEquals(7, map.getTileID(2, 0))
        assertEquals(8, map.getTileID(0, 1))
        assertEquals(9, map.getTileID(1, 1))
        assertEquals(10, map.getTileID(2, 1))
        assert(map.isTileWalkable(0, 0))
        assert(map.isTileShootable(0, 0))
        assert(!map.isTileWalkable(1, 0))
        assert(map.isTileShootable(1, 0))
        assert(!map.isTileWalkable(2, 0))
        assert(!map.isTileShootable(2, 0))
        assertDoesNotThrow { map.isTileWalkable(3, 0) }
        assertDoesNotThrow { map.isTileShootable(3, 0) }
        assertDoesNotThrow { map.isTileWalkable(2, 1) }
        assertDoesNotThrow { map.isTileShootable(2, 1) }
    }
}