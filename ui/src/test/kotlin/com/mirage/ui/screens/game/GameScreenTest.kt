package com.mirage.ui.screens.game

import com.mirage.core.game.objects.SimplifiedState
import com.mirage.core.messaging.ClientMessage
import com.mirage.core.messaging.InitialGameStateMessage
import com.mirage.core.utils.*
import com.mirage.core.VirtualScreen
import com.mirage.core.virtualscreen.GdxVirtualScreen
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class GameScreenTest {

    @Disabled
    @Test
    fun testSimpleStateRendering() {
        val mock = createVirtualScreenMock(TILE_WIDTH + 10f, TILE_HEIGHT + 10f)
        val oneObject = TestSamples.TEST_BUILDING.with(
                template = "wall",
                x = -0.5f,
                y = -0.5f,
                width = 0f,
                height = 0f,
                transparencyRange = 0f
        )
        val twoObject = TestSamples.TEST_ENTITY.with(
                template = "test-entity-1",
                x = 0.5f,
                y = 0.5f
        )
        val oneObjectState = SimplifiedState(listOf(oneObject), listOf(twoObject))
        var lastMsg: ClientMessage? = null
        var msgCount = 0
        val gameScreen = GameScreen(mock, "one-tile-test") {
            lastMsg = it
            ++msgCount
        }
        gameScreen.handleServerMessage(InitialGameStateMessage("one-tile-test", oneObjectState, 0L, 0L))
        gameScreen.render(mock)
        verify(mock, times(1)).drawTile(eq(1), eq(0f), eq(-DELTA_CENTER_Y))
        verify(mock, times(8)).drawTile(eq(0), any(), any())
        verify(mock, times(1)).draw(eq("objects/wall"), any(), any())
        verify(mock, times(1)).draw(eq("objects/wall"), eq(-TILE_WIDTH), eq(-DELTA_CENTER_Y))
        assertNull(lastMsg)
        assertEquals(0, msgCount)
    }


    private fun createVirtualScreenMock(width: Float, height: Float, realWidth: Float = width, realHeight: Float = height): VirtualScreen {
        val mock: GdxVirtualScreen = mock()
        whenever(mock.width) doReturn width
        whenever(mock.height) doReturn height
        whenever(mock.realWidth) doReturn realWidth
        whenever(mock.realHeight) doReturn realHeight
        whenever(mock.projectRealPointOnVirtualScreen(any())).thenCallRealMethod()
        whenever(mock.createLabel(any())).thenReturn(mock<VirtualScreen.Label>())
        whenever(mock.createLabel(any(), any<Float>())).thenReturn(mock<VirtualScreen.Label>())
        whenever(mock.createLabel(any(), any<Rectangle>())).thenReturn(mock<VirtualScreen.Label>())
        whenever(mock.createLabel(any(), any(), any())).thenReturn(mock<VirtualScreen.Label>())
        return mock
    }
}