package com.mirage.core.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class RectangleTest {

    @Test
    fun testRectangle() {
        val rect = Rectangle(2f, 2f, 4f, 2f)
        val other = Rectangle(5f, 1f, 4f, 4f)
        assert(rect.overlaps(other))
        assert(rect.contains(Point(3f, 2.5f)))
        assertEquals(setOf(Point(0f, 1f), Point(4f, 3f), Point(4f, 1f), Point(0f, 3f)), rect.points.toSet())
    }
}