package com.mirage.utils.extensions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MathExtensionsKtTest {

    @Test
    fun testTrunc() {
        assertEquals(-2, (-1.8f).trunc())
        assertEquals(-2, (-1.2f).trunc())
        assertEquals(1, (1.8f).trunc())
        assertEquals(1, (1.2f).trunc())
    }
}