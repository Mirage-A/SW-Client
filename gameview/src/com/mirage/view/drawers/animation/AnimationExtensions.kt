package com.mirage.view.drawers.animation

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.utils.datastructures.MutablePoint
import kotlin.math.min

internal fun getAnimationCurrentTime(animation: Animation, timePassed: Long) =
        if (animation.isRepeatable) timePassed % animation.duration
        else min(timePassed, animation.duration - 1L)

internal fun getStartFrame(frames: List<Animation.Frame>, duration: Int, currentTime: Long) : Animation.Frame =
        frames[(currentTime * (frames.size - 1) / duration).toInt()]

internal fun getEndFrame(frames: List<Animation.Frame>, duration: Int, currentTime: Long) : Animation.Frame =
        frames[min((currentTime * (frames.size - 1) / duration).toInt() + 1, frames.size - 1)]

internal fun getAnimationProgress(frames: List<Animation.Frame>, duration: Int, currentTime: Long) : Float {
    val interval = duration / (frames.size - 1f)
    return currentTime % interval / interval
}

/**
 * Отрисовывает слой изображения (среднее состояние слоя между startLayer и endLayer)
 */
internal fun drawLayer(batch: SpriteBatch, texture: Texture, x: Float, y: Float, startLayer: Animation.Layer, endLayer : Animation.Layer, progress: Float) {
    val angle1 = (startLayer.angle % (2 * Math.PI)).toFloat()
    var angle2 = (endLayer.angle % (2 * Math.PI)).toFloat()
    if (angle2 > angle1) {
        if (angle1  - angle2  + 2 * Math.PI < angle2 - angle1) {
            angle2 -= 2 * Math.PI.toFloat()
        }
    }
    if (angle2 < angle1) {
        if (angle2  - angle1  + 2 * Math.PI < angle1 - angle2) {
            angle2 += 2 * Math.PI.toFloat()
        }
    }
    batch.draw(texture,
            x + curValue(startLayer.x, endLayer.x, progress) - curValue(startLayer.basicWidth, endLayer.basicWidth, progress) / 2,
            y - curValue(startLayer.y, endLayer.y, progress) - curValue(startLayer.basicHeight, endLayer.basicHeight, progress) / 2,
            curValue(startLayer.basicWidth, endLayer.basicWidth, progress) / 2,
            curValue(startLayer.basicHeight, endLayer.basicHeight, progress) / 2,
            curValue(startLayer.basicWidth, endLayer.basicWidth, progress),
            curValue(startLayer.basicHeight, endLayer.basicHeight, progress),
            curValue(startLayer.scale * startLayer.scaleX, endLayer.scale * endLayer.scaleX, progress),
            curValue(startLayer.scale * startLayer.scaleY, endLayer.scale * endLayer.scaleY, progress),
            curValue(Math.toDegrees(angle1.toDouble()).toFloat(), Math.toDegrees(angle2.toDouble()).toFloat(), progress),
            0, 0,
            startLayer.basicWidth,
            startLayer.basicHeight, false, false)
}



/**
 * Находит слой в кадре по названию и возвращает его номер
 * @return Индекс слоя с данным названием (если он присутствует в кадре) или -1 иначе
 */
internal fun findLayer(frame : Animation.Frame, layerName: String) : Int {
    for (layerIndex in frame.layers.indices) {
        if (frame.layers[layerIndex].getName() == layerName) {
            return layerIndex
        }
    }
    return -1
}

/**
 * Возвращает "среднее" значение между startValue и endValue,
 * где "прогресс перехода" равен progress
 * Например, если progress = 0, то возвращается startValue
 * Если progress = 1, то возвращается endValue
 * Если progress = 0.5, то возвращается их среднее арифметическое и т.д.
 */
internal fun curValue(startValue: Float, endValue : Float, progress : Float) : Float {
    return startValue + (endValue - startValue) * progress
}

/**
 * Аналогично, но границы типа Int
 */
internal fun curValue(startValue: Int, endValue : Int, progress : Float) : Float {
    return startValue + (endValue - startValue) * progress
}

/**
 * Аналогично, но возвращает точку между двумя точками (покоординатное curValue)
 */
internal fun curValue(startPoint: MutablePoint, endPoint: MutablePoint, progress: Float) : MutablePoint {
    return MutablePoint(curValue(startPoint.x, endPoint.x, progress), curValue(startPoint.y, endPoint.y, progress))
}