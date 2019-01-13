package com.mirage.controller

object Platform {
    enum class Types {
        DESKTOP,
        ANDROID,
        IOS
    }

    /**
     * Платформа, на которой запущена игра
     */
    var TYPE = Types.DESKTOP
    /**
     * Путь до папки с ресурсами, для разных платформ он разный
     */
    var ASSETS_PATH = ""
}
