package com.mirage.view.drawers.animation

import com.mirage.core.utils.Point
import com.mirage.core.VirtualScreen
import kotlin.math.min

internal fun getAnimationCurrentTime(animation: Animation, timePassed: Long) =
        if (animation.isRepeatable) timePassed % animation.duration
        else min(timePassed, animation.duration - 1L)

internal fun getStartFrame(frames: List<Animation.Frame>, duration: Int, currentTime: Long): Animation.Frame =
        if (frames.isEmpty()) Animation.Frame()
        else frames[(currentTime * (frames.size - 1) / duration).toInt()]

internal fun getEndFrame(frames: List<Animation.Frame>, duration: Int, currentTime: Long): Animation.Frame =
        if (frames.isEmpty()) Animation.Frame()
        else frames[min((currentTime * (frames.size - 1) / duration).toInt() + 1, frames.size - 1)]

internal fun getAnimationProgress(frames: List<Animation.Frame>, duration: Int, currentTime: Long): Float {
    if (frames.size <= 1) return 0f
    val interval = duration / (frames.size - 1f)
    return currentTime % interval / interval
}

/** Draws animation layer at interpolated position */
internal fun drawLayer(virtualScreen: VirtualScreen, textureName: String, x: Float, y: Float, startLayer: Animation.Layer, endLayer: Animation.Layer, progress: Float, scale: Float = 1f) {
    val angle1 = (startLayer.angle % (2 * Math.PI)).toFloat()
    var angle2 = (endLayer.angle % (2 * Math.PI)).toFloat()
    if (angle2 > angle1) {
        if (angle1 - angle2 + 2 * Math.PI < angle2 - angle1) {
            angle2 -= 2 * Math.PI.toFloat()
        }
    }
    if (angle2 < angle1) {
        if (angle2 - angle1 + 2 * Math.PI < angle1 - angle2) {
            angle2 += 2 * Math.PI.toFloat()
        }
    }
    virtualScreen.draw(
            textureName = textureName,
            x = x + curValue(startLayer.x, endLayer.x, progress) * scale,
            y = y - curValue(startLayer.y, endLayer.y, progress) * scale,
            scale = curValue(startLayer.scale, endLayer.scale, progress) * scale,
            scaleX = curValue(startLayer.scaleX, endLayer.scaleX, progress),
            scaleY = curValue(startLayer.scaleY, endLayer.scaleY, progress),
            angle = curValue(angle1, angle2, progress),
            flipX = startLayer.flipX
    )
}


/** Finds a layer with name [layerName] in [frame] or returns -1 if it is absent */
internal fun findLayer(frame: Animation.Frame, layerName: String): Int {
    for (layerIndex in frame.layers.indices) {
        if (frame.layers[layerIndex].getName() == layerName) {
            return layerIndex
        }
    }
    return -1
}

/** Returns value, interpolated between [startValue] and [endValue] with interpolation progress [progress] */
internal fun curValue(startValue: Float, endValue: Float, progress: Float): Float {
    return startValue + (endValue - startValue) * progress
}

/** @see curValue */
internal fun curValue(startValue: Int, endValue: Int, progress: Float): Float {
    return startValue + (endValue - startValue) * progress
}

/** @see curValue */
internal fun curValue(startPoint: Point, endPoint: Point, progress: Float): Point =
        Point(curValue(startPoint.x, endPoint.x, progress), curValue(startPoint.y, endPoint.y, progress))