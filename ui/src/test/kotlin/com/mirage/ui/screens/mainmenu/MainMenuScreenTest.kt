package com.mirage.ui.screens.mainmenu

import com.mirage.core.utils.Rectangle
import com.mirage.core.messaging.ClientMessage
import com.mirage.core.messaging.ExitClientMessage
import com.mirage.core.VirtualScreen
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class MainMenuScreenTest {

    /*
    @Disabled
    @Test
    fun testMainMenuRendering() {
        val mock = createVirtualScreenMock(1600f, 900f)
        var lastMsg: ClientMessage? = null
        var msgCount = 0
        val menuScreen = MainMenuScreen(mock) {
            lastMsg = it
            ++msgCount
        }
        menuScreen.render(mock)
        verify(mock, times(3)).draw(eq("ui/main-menu-btn"), any())
        verify(mock, times(1)).draw(eq("ui/art"), eq(0f), eq(0f), any(), any())
        clearInvocations(mock)
        menuScreen.touchDown(800, 850, 0, 0)
        menuScreen.render(mock)
        verify(mock, times(2)).draw(eq("ui/main-menu-btn"), any())
        verify(mock, times(1)).draw(eq("ui/main-menu-btn-pressed"), any())
        verify(mock, times(1)).draw(eq("ui/art"), eq(0f), eq(0f), any(), any())
        clearInvocations(mock)
        assertEquals(0, msgCount)
        assertEquals(null, lastMsg)
        menuScreen.touchUp(800, 850, 0, 0)
        menuScreen.mouseMoved(800, 850)
        menuScreen.render(mock)
        verify(mock, times(2)).draw(eq("ui/main-menu-btn"), any())
        verify(mock, times(1)).draw(eq("ui/main-menu-btn-highlighted"), any())
        verify(mock, times(1)).draw(eq("ui/art"), eq(0f), eq(0f), any(), any())
        clearInvocations(mock)
        assertEquals(1, msgCount)
        assertTrue(lastMsg is ExitClientMessage)

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
    }*/
}