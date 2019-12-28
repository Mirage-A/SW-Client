package com.mirage.logic.state

import com.mirage.core.game.objects.SimplifiedObject

interface ExtendedObject : SimplifiedObject {

    override var template: String

    override var x: Float

    override var y: Float


}