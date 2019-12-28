package com.mirage.core.datastructures

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PointTest {

    @Test
    fun testPoint() {
        val p = Point(0f, 0f)
        assert(p.move(Math.PI.toFloat() / 2f, 1f) near Point(0f, 1f))
        assertEquals(Point(2f, 2f), p + Point(1f, 2f) - Point(-1f, 0f))
        assertEquals("[0.0, 0.0]", p.toString())
    }
}