package com.mirage.configuration

import org.apache.log4j.Logger

/**
 * Обёртка над Logger-ом из log4j.
 * Делает все методы статическими и автоматически запускает конфигурацию при первом вызове,
 * чтобы сократить код, связанный с логированием.
 */
object Log {

    private val log = Logger.getLogger("")
    private var configured = false

    /**
     * Конфигурация Logger-а.
     * Запускается при первом логировании.
     */
    private fun configure() {
        if (!configured) {
            // Костыль для логирования
            // TODO сделать нормальный файл с настройками
            org.apache.log4j.BasicConfigurator.configure()
            configured = true
        }
    }

    fun i(msg: Any) {
        configure()
        log.info(msg)
    }

    fun d(msg: Any) {
        configure()
        log.debug(msg)
    }

    fun w(msg: Any) {
        configure()
        log.warn(msg)
    }

    fun e(msg: Any) {
        configure()
        log.error(msg)
    }

    fun f(msg: Any) {
        configure()
        log.fatal(msg)
    }
}
