package com.mirage.view

import com.mirage.core.TILE_HEIGHT
import com.mirage.core.TILE_WIDTH
import com.mirage.core.game.maps.SceneLoader
import com.mirage.core.VirtualScreen
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MapRendererKtTest {

    /*
    @Test
    fun testMockito() {
        val mock: VirtualScreen = mock()
        mock.draw("null", 0f, 0f)
        verify(mock, after(100L).times(1)).draw(any(), eq(0f), eq(0f))
        verifyNoMoreInteractions(mock)
    }

    @Test
    fun testSmallMapRendering() {
        val gameMap = SceneLoader("micro-test").loadMap()
        val list = Array(5) { "null" }.toList()
        assertEquals(list[0], list[0])
        val mock: VirtualScreen = mock {
            on { width } doReturn TILE_WIDTH * 3 / 2
            on { height } doReturn TILE_HEIGHT * 3 / 2
        }
        renderGameMap(
                virtualScreen = mock,
                gameMap = gameMap,
                cameraX = 0f,
                cameraY = 0f
        )
        verify(mock, times(16)).drawTile(any(), any(), any())
        verify(mock, times(4)).drawTile(eq(4), any(), any())
        verify(mock, times(12)).drawTile(eq(1), any(), any())
        verify(mock, times(4)).drawTile(any(), eq(0f), any())
        verify(mock, times(3)).drawTile(any(), eq(-TILE_WIDTH / 2f), any())
        verify(mock, times(2)).drawTile(any(), eq(-TILE_WIDTH), any())
        verify(mock, times(1)).drawTile(any(), eq(-TILE_WIDTH * 1.5f), any())
        verify(mock, times(4)).drawTile(any(), any(), eq(0f))
        verify(mock, times(1)).width
        verify(mock, times(1)).height
        verifyNoMoreInteractions(mock)
    }*/

}