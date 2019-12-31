package com.mirage.client

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class GdxAssetsTest {

    @Test
    fun testFileLoader() {
        assertDoesNotThrow {
            GdxAssets.loadFile("wtfisthis/idontexist!.ppnngg")
        }
        assertDoesNotThrow {
            GdxAssets.loadFile("C:\\Windows\\System32\\ActiveHours.png")
        }
        assertNotNull(GdxAssets.loadFile("drawable/objects/null.png"))
    }


}