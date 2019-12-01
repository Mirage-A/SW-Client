package com.mirage.utils.game.objects.extended

import com.mirage.utils.game.objects.simplified.SimplifiedBuilding

class ExtendedBuilding(
        override var template: String = "",
        override var x: Float = 0f,
        override var y: Float = 0f,
        override var width: Float = 0f,
        override var height: Float = 0f,
        override var transparencyRange: Float = 0f,
        override var state: String = "default",
        var isRigid: Boolean = false
) : ExtendedObject, SimplifiedBuilding(template, x, y, width, height, transparencyRange, state)