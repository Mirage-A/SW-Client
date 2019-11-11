package com.mirage.utils

import org.junit.jupiter.api.Test

internal class TimerTest {

    @Test
    fun test() {
        var a = 0
        val timer = Timer(10L) {
            ++a
        }
        timer.start()
        Thread.sleep(50L)
        timer.pause()
        assert(a in 4..6)
        timer.resume()
        Thread.sleep(50L)
        timer.stop()
        assert(a in 8..12)
    }
}