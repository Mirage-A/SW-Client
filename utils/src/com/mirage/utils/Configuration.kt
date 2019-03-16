package com.mirage.utils

import com.mirage.utils.datastructures.Point

const val INTERPOLATION_DELAY_MILLIS = 200L

const val MAX_EXTRAPOLATION_INTERVAL = 1000L

const val GAME_LOOP_TICK_INTERVAL = 50L

const val DESKTOP_FULL_SCREEN = true

val DEFAULT_MAP_POINT = Point(0f, 0f)

val config = hashMapOf<String, Any?>(
        "debug" to true,
        "show-invisible-objects" to false, //TODO Этот режим забаганный
        "platform" to "",
        "tile-height" to 64f
)