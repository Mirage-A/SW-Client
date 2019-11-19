package com.mirage.utils

import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.graphics.Texture
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AssetsTest {


    @Test
    fun testNothing() {
        assertTrue(true)
    }
    //TODO
    /*
    @Test
    fun testFileLoader() {
        assertDoesNotThrow {
            Assets.loadFile("wtfisthis/idontexist!.ppnngg")
        }
        assertDoesNotThrow {
            Assets.loadFile("C:\\Windows\\System32\\ActiveHours.png")
        }
        assertNotNull(Assets.loadFile("drawable/objects/null.png"))
    }

    @Test
    fun testScriptLoader() {
        assertDoesNotThrow {
            Assets.loadScript("wtfisthis/idontexist!.ppnngg")
        }
        assertEquals("println(\"Hello world!\")", Assets.loadScript("test")!!.readText())
    }

    @Test
    fun testTextureLoading() {
        assertDoesNotThrow {
            Assets.getRawTexture("wtfisthis/idontexist!.ppnngg")
        }
        assertNotNull(Assets.getRawTexture("objects/null"))
    }

    @Test
    fun testTileTexturesLoading() {
        assertDoesNotThrow {
            Assets.loadTileTexturesList("wtfisthis/idontexist!.ppnngg")
        }
        val walkabilityList = Assets.loadTileTexturesList("walkability")
        assertEquals(3, walkabilityList.size)
        for (texture in walkabilityList) {
            assertEquals(TILE_WIDTH, texture.regionWidth)
            assertEquals(TILE_HEIGHT, texture.regionHeight)
        }
    }*/


}