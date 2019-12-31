package com.mirage.client

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.mirage.core.utils.ClientPlatform
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test

internal class GdxPreferencesTest {

    @Test
    fun testProfileInfo() {
        assertDoesNotThrow { GdxPreferences }
        assertDoesNotThrow { GdxPreferences.savePreferences() }
    }

    @Test
    fun testHeadlessProfileInfo() {
        val appListener: ApplicationListener = object : ApplicationListener {
            override fun render() {}
            override fun pause() {}
            override fun resume() {}
            override fun resize(width: Int, height: Int) {}
            override fun create() {}
            override fun dispose() {}
        }
        val app = HeadlessApplication(appListener)

        ClientPlatform.platform = ClientPlatform.DESKTOP

        assertDoesNotThrow { GdxPreferences }
        assertDoesNotThrow { GdxPreferences.savePreferences() }

        ClientPlatform.platform = ClientPlatform.TEST

        app.exit()

        assert(true)
    }
}