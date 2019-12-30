package com.mirage.client

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.mirage.core.utils.PLATFORM
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

        val platform = PLATFORM
        PLATFORM = "desktop"

        assertDoesNotThrow { GdxPreferences }
        assertDoesNotThrow { GdxPreferences.savePreferences() }

        PLATFORM = platform

        app.exit()

        assert(true)
    }
}