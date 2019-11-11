package com.mirage.utils.datastructures

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class IntDSUTest {

    @Test
    fun testIntDSU() {
        val dsu = IntDSU(4)
        assertEquals(0, dsu.findRoot(0))
        assertEquals(1, dsu.findRoot(1))
        assertEquals(2, dsu.findRoot(2))
        dsu.unite(0, 1)
        assertEquals(0, dsu.findRoot(0))
        assertEquals(0, dsu.findRoot(1))
        dsu.unite(1, 2)
        assertEquals(0, dsu.findRoot(2))
    }

}