package com.mirage.utils.game.maps

import com.mirage.utils.datastructures.Rectangle

data class ScriptArea(val area: Rectangle, val playersOnly: Boolean, val onEnter: String?, val onLeave: String?)