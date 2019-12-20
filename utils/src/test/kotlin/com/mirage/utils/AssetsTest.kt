package com.mirage.utils

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class AssetsTest {


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


}