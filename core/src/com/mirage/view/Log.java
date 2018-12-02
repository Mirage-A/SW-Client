package com.mirage.view;

import org.apache.log4j.Logger;

/**
 * Обёртка над Logger-ом из log4j.
 * Делает все методы статическими и автоматически запускает конфигурацию при первом вызове,
 * чтобы сократить код, связанный с логированием.
 */
public class Log {

    private static final Logger log = Logger.getLogger("");
    private static boolean configured = false;

    /**
     * Конфигурация Logger-а.
     * Запускается при первом логировании.
     */
    private static void configure() {
        if (!configured) {
            // Костыль для логирования
            // TODO сделать нормальный файл с настройками
            org.apache.log4j.BasicConfigurator.configure();
            configured = true;
        }
    }

    public static void i(Object msg) {
        configure();
        log.info(msg);
    }

    public static void d(Object msg) {
        configure();
        log.debug(msg);
    }

    public static void w(Object msg) {
        configure();
        log.warn(msg);
    }

    public static void e(Object msg) {
        configure();
        log.error(msg);
    }

    public static void f(Object msg) {
        configure();
        log.fatal(msg);
    }
}
