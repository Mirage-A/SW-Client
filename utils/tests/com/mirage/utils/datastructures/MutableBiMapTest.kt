package com.mirage.utils.datastructures

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MutableBiMapTest {

    @Test
    fun test() {
        val biMap = MutableBiMap<Int, String, Long>()
        assertEquals(0, biMap.size)
        biMap[0, "wtf"] = 2L
        assertEquals(2L, biMap[0, "wtf"])
        assertEquals(null, biMap[0, "a"])
        assertEquals(null, biMap[1, "wtf"])
        assertEquals(1, biMap.size)
        biMap.remove(1, "wtf")
        assertEquals(1, biMap.size)
        biMap.remove(0, "wtf")
        assertEquals(0, biMap.size)
        assertTrue(biMap.isEmpty())
        biMap[1, "hi"] = 1L
        biMap[0, "hi"] = 5L
        biMap[1, "hi"] = 6L
        assertEquals(2, biMap.size)
        assertEquals(6L, biMap[1, "hi"])
        assertEquals(5L, biMap[0, "hi"])
        assertFalse(biMap.isEmpty())
        assertTrue(biMap.containsKey(0, "hi"))
        assertFalse(biMap.containsKey(0, "wtf"))
        assertTrue(biMap.containsValue(6L))
        assertFalse(biMap.containsValue(1L))
        biMap.clear()
        assertTrue(biMap.isEmpty())
    }
}