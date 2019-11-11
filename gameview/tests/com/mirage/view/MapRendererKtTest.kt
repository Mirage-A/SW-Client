package com.mirage.view

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.mirage.utils.TILE_HEIGHT
import com.mirage.utils.TILE_WIDTH
import com.mirage.utils.game.maps.SceneLoader
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import kotlin.math.roundToInt

internal class MapRendererKtTest {

    @Test
    fun testMockito() {
        val mock: SpriteBatch = mock()
        mock.draw(TextureRegion(), 0f, 0f)
        verify(mock, after(100L).times(1)).draw(any<TextureRegion>(), eq(0f), eq(0f))
        verifyNoMoreInteractions(mock)
    }

    @Test
    fun testSmallMapRendering() {
        val gameMap = SceneLoader.loadScene("micro-test").first
        val list = Array(5) {TextureRegion()}.toList()
        assertEquals(list[0], list[0])
        assertNotEquals(list[0], list[1])
        val mock: SpriteBatch = mock()
        renderGameMap(
                batch = mock,
                gameMap = gameMap,
                tileTexturesList = list,
                cameraX = 0f,
                cameraY = 0f,
                virtualScreenWidth = TILE_WIDTH.roundToInt() * 3 / 2,
                virtualScreenHeight = TILE_HEIGHT.roundToInt() * 3 / 2
        )
        verify(mock, times(16)).draw(any<TextureRegion>(), any(), any())
        verify(mock, times(4)).draw(eq(list[4]), any(), any())
        verify(mock, times(12)).draw(eq(list[1]), any(), any())
        verify(mock, times(4)).draw(any<TextureRegion>(), eq(-TILE_WIDTH / 2f), any())
        verify(mock, times(3)).draw(any<TextureRegion>(), eq(-TILE_WIDTH), any())
        verify(mock, times(2)).draw(any<TextureRegion>(), eq(-TILE_WIDTH * 3f / 2f), any())
        verify(mock, times(1)).draw(any<TextureRegion>(), eq(-TILE_WIDTH * 2f), any())
        verify(mock, times(4)).draw(any<TextureRegion>(), any(), eq(-TILE_HEIGHT / 2f))
        verifyNoMoreInteractions(mock)
    }

}