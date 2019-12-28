package com.mirage.view.drawers.templates

import com.mirage.core.Log
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.game.objects.properties.WeaponType
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.view.drawers.DrawerTemplate
import com.mirage.view.drawers.animation.*

class AnimationDrawerTemplate(animationName: String) : DrawerTemplate {

    private val animation = AnimationLoader.getObjectAnimation(animationName)


    override fun draw(virtualScreen: VirtualScreen, x: Float, y: Float, isOpaque: Boolean, action: String, actionTimePassedMillis: Long, isMoving: Boolean, movingTimePassedMillis: Long, moveDirection: MoveDirection) {
        if (!isOpaque) return

        val frames: List<Animation.Frame> = animation.data[MoveDirection.RIGHT]?.get(WeaponType.UNARMED) ?: run {
            Log.e("Error while loading object animation")
            return
        }

        if (frames.isEmpty()) return

        val time = getAnimationCurrentTime(animation, actionTimePassedMillis)
        val startFrame = getStartFrame(frames, animation.duration, time)
        val endFrame = getEndFrame(frames, animation.duration, time)
        val progress = getAnimationProgress(frames, animation.duration, time)

        for (layerIndex in 0 until startFrame.layers.size) {
            val startLayer = startFrame.layers[layerIndex]
            val endLayer = endFrame.layers[layerIndex]
            val layerName = startLayer.getName()

            drawLayer(virtualScreen, layerName, x, y, startLayer, endLayer, progress)
        }
    }

}