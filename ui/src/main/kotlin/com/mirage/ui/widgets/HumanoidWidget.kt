package com.mirage.ui.widgets

import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.VirtualScreen
import com.mirage.core.utils.Assets
import com.mirage.ui.fragments.inventory.centerColumnWidthPart
import com.mirage.ui.fragments.inventory.humanoidScale
import com.mirage.ui.fragments.inventory.humanoidSize
import com.mirage.view.drawers.animation.AnimationLoader
import com.mirage.view.drawers.templates.HumanoidDrawerTemplate

/** Draws humanoid in inventory widget */
internal class HumanoidWidget(assets: Assets) : Widget {

    private val animationLoader = AnimationLoader(assets)

    var equipment: Equipment = Equipment()
        set(value) {
            drawer = HumanoidDrawerTemplate(animationLoader, value, humanoidScale)
            field = value
        }

    private var drawer = HumanoidDrawerTemplate(animationLoader, equipment, humanoidScale)

    override var isVisible: Boolean = true

    override fun draw(virtualScreen: VirtualScreen) {
        val w = virtualScreen.width
        drawer.draw(
                virtualScreen,
                -w * centerColumnWidthPart / 2f - (w / 2f - w * centerColumnWidthPart / 2f) / 2f,
                -humanoidSize / 2f,
                0f,
                0f,
                true,
                "idle",
                0L,
                false,
                0L,
                MoveDirection.RIGHT
        )
    }

    override fun resize(virtualWidth: Float, virtualHeight: Float) {}
}