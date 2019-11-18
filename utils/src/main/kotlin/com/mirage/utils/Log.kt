package com.mirage.utils



/**
 * Обёртка над Logger-ом из log4j.
 * Делает все методы статическими и автоматически запускает конфигурацию при первом вызове,
 * чтобы сократить код, связанный с логированием.
 */
object Log {

    fun i(msg: Any) {
        println(msg.toString())
    }

    fun d(msg: Any) {
        println(msg.toString())
    }

    fun e(msg: Any) {
        println(msg.toString())
    }
}
