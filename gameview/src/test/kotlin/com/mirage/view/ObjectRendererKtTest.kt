package com.mirage.view

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.mirage.utils.TILE_WIDTH
import com.mirage.utils.TestSamples
import com.mirage.utils.game.objects.GameObjects
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test

internal class ObjectRendererKtTest {

    @Test
    fun testSmallStateRendering() {
        //TODO Переделать тест
        /*
        val obj = TestSamples.TEST_GAME_OBJECT.with(
                x = 1f,
                y = 1f
        )
        val objs = GameObjects(hashMapOf(0L to obj), 1L)
        val drawer = object : ObjectDrawer {
            override fun draw(batch: SpriteBatch, x: Float, y: Float) = batch.draw(TextureRegion(), x, y)
        }
        val drawers = Drawers()
        drawers[0L] = drawer

        val mock : SpriteBatch = mock()
        renderObjects(mock, objs, drawers, 100f, 100f)

        verify(mock, times(1)).draw(any<TextureRegion>(), eq(TILE_WIDTH - 100f), eq(-100f))
        verifyNoMoreInteractions(mock)*/
    }
}