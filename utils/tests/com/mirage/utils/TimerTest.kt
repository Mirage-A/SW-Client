package com.mirage.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TimerTest {

    @Test
    fun test() {
        var a = 0
        val timer = Timer(5L) {
            ++a
        }
        timer.start()
        Thread.sleep(27L)
        timer.pause()
        assertEquals(5, a)
        timer.resume()
        Thread.sleep(27L)
        timer.stop()
        assertEquals(10, a)
    }
}