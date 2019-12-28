package com.mirage.core.utils

import com.mirage.core.utils.sqr
import com.mirage.core.utils.trunc
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MathExtensionsKtTest {

    @Test
    fun testTrunc() {
        assertEquals(-2, (-1.8f).trunc())
        assertEquals(-2, (-1.2f).trunc())
        assertEquals(1, (1.8f).trunc())
        assertEquals(1, (1.2f).trunc())
        assertEquals(4f, sqr(2f))
    }
}