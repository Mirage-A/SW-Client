package com.mirage.utils.game.objects.extended

import com.mirage.utils.game.objects.simplified.SimplifiedObject

interface ExtendedObject : SimplifiedObject {

    override var template: String

    override var x: Float

    override var y: Float


}