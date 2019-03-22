package com.mirage.utils

import kotlinx.coroutines.*
import java.util.concurrent.locks.ReentrantLock

/**
 * Таймер, который работает в отдельной корутине и каждые delay мс вызывает listener.
 * Если listener работает слишком долго, следующий вызов произойдет сразу же после окончания текущего.
 * В listener как аргумент передаётся время в наносекундах, прошедшее с прошлого вызова.
 */
class CoroutineTimer(private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + Job()),
                     private val delayMillis: Long,
                     private val updateListener: suspend (deltaMillis: Long) -> Unit) {

    private val lock = ReentrantLock()

    private var lastTickTime = System.currentTimeMillis()

    private var isStopped = false

    /**
     * Запускает таймер и начинает повторения [updateListener]
     */
    fun start() = scope.launch {
        lastTickTime = System.currentTimeMillis()
        while(!isStopped) {
            lock.lock()
            val curTime = System.currentTimeMillis()
            updateListener(curTime - lastTickTime)
            val deltaMillis = System.currentTimeMillis() - curTime
            if (deltaMillis < delayMillis) {
                delay(delayMillis - deltaMillis)
            }
            lastTickTime = curTime
            lock.unlock()
        }
    }

    /**
     * Дожидается выполнения текущего [updateListener],
     * затем приостанавливает повторение вызовов, пока не будет вызван [resume].
     */
    fun pause() = lock.lock()

    /**
     * Продолжает повторение вызовов [updateListener] после вызова [pause].
     */
    fun resume() {
        lastTickTime = System.currentTimeMillis()
        lock.unlock()
    }

    /**
     * Полностью останавливает таймер. После вызова этого метода таймер перезапустить невозможно.
     */
    fun stop() {
        isStopped = true
    }
}