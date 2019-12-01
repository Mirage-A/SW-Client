package com.mirage.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

internal class LoopTimerTest {

    @Test
    fun testLoopTimer() {
        val counter = AtomicInteger(0)
        val timer = LoopTimer(50L) {
            println(it)
            assert(it in 0L..1L || it in 48L..52L)
            counter.incrementAndGet()
        }
        timer.start()
        Thread.sleep(475L)
        timer.stop()
        assertEquals(10, counter.get())
    }
}