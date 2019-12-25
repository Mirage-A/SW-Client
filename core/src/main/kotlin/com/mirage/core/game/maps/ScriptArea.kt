package com.mirage.core.game.maps

import com.mirage.core.datastructures.Rectangle

data class ScriptArea(val area: Rectangle, val playersOnly: Boolean, val onEnter: String?, val onLeave: String?)