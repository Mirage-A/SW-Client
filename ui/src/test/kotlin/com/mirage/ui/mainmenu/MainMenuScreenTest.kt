package com.mirage.ui.mainmenu

import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ExitClientMessage
import com.mirage.utils.virtualscreen.VirtualScreen
import com.mirage.utils.virtualscreen.VirtualScreenGdxImpl
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MainMenuScreenTest {

    @Test
    fun testMainMenuRendering() {
        val mock = createVirtualScreenMock(1600f, 900f)
        val menuScreen = MainMenuScreen(mock)
        var lastMsg: ClientMessage? = null
        var msgCount = 0
        menuScreen.inputMessages.subscribe {
            lastMsg = it
            ++msgCount
        }
        menuScreen.render(mock, 0L)
        verify(mock, times(4)).draw(eq("ui/mainmenubtn"), any())
        verify(mock, times(1)).draw(eq("ui/art"), eq(0f), eq(0f), any(), any())
        clearInvocations(mock)
        menuScreen.inputProcessor.touchDown(800, 850, 0, 0)
        menuScreen.render(mock, 0L)
        verify(mock, times(3)).draw(eq("ui/mainmenubtn"), any())
        verify(mock, times(1)).draw(eq("ui/mainmenubtnpressed"), any())
        verify(mock, times(1)).draw(eq("ui/art"), eq(0f), eq(0f), any(), any())
        clearInvocations(mock)
        assertEquals(0, msgCount)
        assertEquals(null, lastMsg)
        menuScreen.inputProcessor.touchUp(800, 850, 0, 0)
        menuScreen.inputProcessor.mouseMoved(800, 850)
        menuScreen.render(mock, 0L)
        verify(mock, times(3)).draw(eq("ui/mainmenubtn"), any())
        verify(mock, times(1)).draw(eq("ui/mainmenubtnhighlighted"), any())
        verify(mock, times(1)).draw(eq("ui/art"), eq(0f), eq(0f), any(), any())
        clearInvocations(mock)
        assertEquals(1, msgCount)
        assertTrue(lastMsg is ExitClientMessage)

    }

    private fun createVirtualScreenMock(width: Float, height: Float, realWidth: Float = width, realHeight: Float = height) : VirtualScreen {
        val mock: VirtualScreenGdxImpl = mock()
        whenever(mock.width) doReturn width
        whenever(mock.height) doReturn height
        whenever(mock.realWidth) doReturn realWidth
        whenever(mock.realHeight) doReturn realHeight
        whenever(mock.projectRealPointOnVirtualScreen(any())).thenCallRealMethod()
        return mock
    }
}