package com.mirage.view.gameobjects

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.utils.messaging.MoveDirection
import com.mirage.utils.datastructures.MutablePoint
import com.mirage.view.TextureLoader
import com.mirage.view.animation.*


/**
 * Анимация объекта из редактора анимаций
 * Загружается из файла assets/animations/OBJECT/${name}.swa
 */
class ObjectAnimation(name: String) : ObjectDrawer {

    private var startTime: Long = System.currentTimeMillis()

    private val animation: Animation = AnimationLoader.getObjectAnimation(name)
    private val frames : ArrayList<Frame> = animation.data[MoveDirection.RIGHT]!![WeaponType.UNARMED]!!

    /**
     * Метод рисования всего объекта
     */
    override fun draw(batch: SpriteBatch, x: Float, y: Float) {
        val timePassedSinceStart = System.currentTimeMillis() - startTime
        if (frames.isNotEmpty()) {
            val time = when (true) {
                animation.isRepeatable -> timePassedSinceStart % animation.duration
                else -> Math.min(timePassedSinceStart, animation.duration - 1L)
            }
            val startFrame = frames[(time * (frames.size - 1) / animation.duration).toInt()]
            val endFrame = frames[Math.min((time * (frames.size - 1) / animation.duration).toInt() + 1, frames.size - 1)]
            val interval = animation.duration / (frames.size - 1f)
            val progress = time % interval / interval

            for (layerIndex in startFrame.layers.indices) {
                val startLayer = startFrame.layers[layerIndex]
                val endLayer = endFrame.layers[layerIndex]
                drawLayer(batch, x, y, TextureLoader.getStaticTexture(startLayer.imageName.substring(0, startLayer.imageName.length - 4)), startLayer, endLayer, progress)
            }
        }
    }


    /**
     * Отрисовывает слой изображения (среднее состояние слоя между startLayer и endLayer)
     */
    private fun drawLayer(batch: SpriteBatch, x: Float, y: Float, img: Image, startLayer: Layer, endLayer : Layer, progress: Float) {
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
        batch.draw(img.getTexture(),
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
     * Возвращает "среднее" значение между startValue и endValue,
     * где "прогресс перехода" равен progress
     * Например, если progress = 0, то возвращается startValue
     * Если progress = 1, то возвращается endValue
     * Если progress = 0.5, то возвращается их среднее арифметическое и т.д.
     */
    private fun curValue(startValue: Float, endValue : Float, progress : Float) : Float {
        return startValue + (endValue - startValue) * progress
    }

    /**
     * Аналогично, но границы типа Int
     */
    private fun curValue(startValue: Int, endValue : Int, progress : Float) : Float {
        return startValue + (endValue - startValue) * progress
    }

    /**
     * Аналогично, но возвращает точку между двумя точками (покоординатное curValue)
     */
    private fun curValue(startPoint: MutablePoint, endPoint: MutablePoint, progress: Float) : MutablePoint {
        return MutablePoint(curValue(startPoint.x, endPoint.x, progress), curValue(startPoint.y, endPoint.y, progress))
    }

}
