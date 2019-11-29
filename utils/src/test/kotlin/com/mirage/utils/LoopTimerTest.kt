package com.mirage.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

internal class LoopTimerTest {

    @Test
    fun testLoopTimer() {
        val counter = AtomicInteger(0)
        val timer = LoopTimer(10L) {
            println(it)
            assert(it in 0L..1L || it in 9L..11L)
            counter.incrementAndGet()
        }
        timer.start()
        Thread.sleep(95L)
        timer.stop()
        assertEquals(10, counter.get())
    }
}