package com.mirage.utils.virtualscreen

import com.mirage.utils.datastructures.Rectangle
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ScreenSizeCalculatorTest {
    @Test
    fun testScreenScaling() {
        assertEquals(Rectangle(0f, 0f, 128f, 64f),
                calculateTileSize(1920f, 1080f))
        assertEquals(Rectangle(0f, 0f, 1920f, 1080f),
                calculateViewportSize(1920f, 1080f))

        assertEquals(Rectangle(0f, 0f, 64f, 32f),
                calculateTileSize(960f, 540f))
        assertEquals(Rectangle(0f, 0f, 1920f, 1080f),
                calculateViewportSize(1920f, 1080f))

        assertEquals(Rectangle(0f, 0f, 108f, 54f),
                calculateTileSize(1600f, 900f))
        assertEquals(Rectangle(0f, 0f, 1896f, 1066f),
                calculateViewportSize(1600f, 900f))

        assertEquals(Rectangle(0f, 0f, 36f, 18f),
                calculateTileSize(480f, 320f))
        assertEquals(Rectangle(0f, 0f, 1706f, 1138f),
                calculateViewportSize(480f, 320f))
    }

}
