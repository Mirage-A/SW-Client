package com.mirage.utils.game.objects.extended

import com.mirage.utils.game.objects.simplified.SimplifiedBuilding

class ExtendedBuilding(
        override var template: String = "",
        override var x: Float = 0f,
        override var y: Float = 0f,
        override var name: String = "",
        override var width: Float = 0f,
        override var height: Float = 0f,
        override var transparencyRange: Float = 0f,
        override var state: String = "default",
        var isRigid: Boolean = false
) : ExtendedObject, SimplifiedBuilding(template, x, y, name, width, height, transparencyRange, state) {

    fun with(
            template: String = this.template,
            x: Float = this.x,
            y: Float = this.y,
            name: String = this.name,
            width: Float = this.width,
            height: Float = this.height,
            transparencyRange: Float = this.transparencyRange,
            state: String = this.state,
            isRigid: Boolean = this.isRigid
    ): ExtendedBuilding = ExtendedBuilding(template, x, y, name, width, height, transparencyRange, state, isRigid)

    override fun with(template: String, x: Float, y: Float): ExtendedBuilding =
            this.with(
                    template = template,
                    x = x,
                    y = y,
                    isRigid = this.isRigid
            )

}