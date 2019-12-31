package com.mirage.core.utils

import kotlin.concurrent.thread

class LoopTimer(private val delayMillis: Long, private val onUpdate: (TimeMillis, IntervalMillis) -> Unit) {

    @Volatile
    private var isStopped = false

    @Volatile
    private var isPaused = false

    private val lock = Any()

    @Volatile
    private var lastTickTime = Long.MIN_VALUE

    private val thread = thread(false) {
        while (!isStopped) {
            if (!isPaused) {
                synchronized(lock) {
                    val time = System.currentTimeMillis()
                    val deltaTime = if (lastTickTime == Long.MIN_VALUE) 0L else time - lastTickTime
                    onUpdate(time, deltaTime)
                    lastTickTime = time
                    val secondTime = System.currentTimeMillis()
                    val updateTime = secondTime - time
                    if (updateTime < delayMillis) {
                        if (updateTime < delayMillis - 5L) Thread.sleep(delayMillis - updateTime - 5L)
                        while (System.currentTimeMillis() < time + delayMillis) {
                        }
                    }
                }
            } else Thread.sleep(10L)
        }
    }

    fun start() {
        thread.start()
    }

    fun stop() {
        isStopped = true
        synchronized(lock) {}
    }

    fun pause() {
        isPaused = true
        synchronized(lock) {}
    }

    fun resume() {
        isPaused = false
        synchronized(lock) {}
    }

}