package com.mirage.view.objectdrawers

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.utils.datastructures.MutablePoint
import com.mirage.utils.game.objects.GameObject
import com.mirage.view.objectdrawers.animation.*


/**
 * Класс для работы со скелетной анимацией гуманоидов
 * (анимация остальных существ задается покадрово классом SpriteAnimation)
 */
class HumanoidAnimation : ObjectDrawer {
    /**
     * Направление движения (на сцене)
     */
    var moveDirection: GameObject.MoveDirection = GameObject.MoveDirection.RIGHT
    /**
     * Тип оружия гуманоида (одноручное, двуручное, два одноручных, одноручное и щит, лук и т.д.)
     */
    var weaponType: GameObject.WeaponType = GameObject.WeaponType.UNARMED

    /**
     * Словарь из текстур экипировки данного гуманоида
     * Должен содержать ключи head[RIGHT, DOWN, etc], body, handtop, handbottom, legtop, legbottom, cloak, weapon1, weapon2
     */
    var textures: MutableMap<String, Image>

    /**
     * Буфер направления движения, необходим для плавности поворота персонажа
     */
    var bufferedMoveDirection = GameObject.MoveDirection.RIGHT

    /**
     * Время последнего изменения направления движения
     */
    var lastMoveDirectionUpdateTime = 0L

    /**
     * Действие, которое анимируется (ожидание, бег, атака и т.д.)
     */
    private var bodyAction = BodyAction.IDLE
    private var legsAction = LegsAction.IDLE
    /**
     * Непосредственно анимации
     */
    private var bodyAnimation = AnimationLoader.getBodyAnimation(BodyAction.IDLE)
    private var legsAnimation = AnimationLoader.getLegsAnimation(LegsAction.IDLE)

    /**
     * Время начала анимации body
     */
    private var bodyStartTime = 0L

    /**
     * Время начала анимации legs
     */
    private var legsStartTime = 0L

    constructor(textures: MutableMap<String, Image>) {
        this.textures = textures
    }

    constructor(textures: MutableMap<String, Image>, bodyAction: BodyAction, legsAction: LegsAction, moveDirection: GameObject.MoveDirection, weaponType: GameObject.WeaponType) {
        this.textures = textures
        this.bodyAction = bodyAction
        this.legsAction = legsAction
        this.moveDirection = moveDirection
        this.weaponType = weaponType
    }


    /**
     * Устанавливает анимацию body
     * Если она изменилась, загружает новую из синглтона AnimationLoader
     */
    fun setBodyAction(action: BodyAction) {
        if (action != bodyAction) {
            bodyAction = action
            bodyStartTime = System.currentTimeMillis()
            bodyAnimation = AnimationLoader.getBodyAnimation(action)
        }
    }

    /**
     * Устанавливает анимацию legs
     * Если она изменилась, загружает новую из синглтона AnimationLoader
     */
    fun setLegsAction(action: LegsAction) {
        if (action != legsAction) {
            legsAction = action
            legsStartTime = System.currentTimeMillis()
            legsAnimation = AnimationLoader.getLegsAnimation(action)
        }
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

    /**
     * Получает направление движения объекта относительно сцены и возвращает направление движения, которое используется для анимации.
     */
    private fun getViewMoveDirection(sceneMoveDirection: GameObject.MoveDirection) : GameObject.MoveDirection =
            when (sceneMoveDirection) {
                GameObject.MoveDirection.RIGHT -> GameObject.MoveDirection.DOWN_RIGHT
                GameObject.MoveDirection.UP_RIGHT -> GameObject.MoveDirection.RIGHT
                GameObject.MoveDirection.UP -> GameObject.MoveDirection.UP_RIGHT
                GameObject.MoveDirection.UP_LEFT -> GameObject.MoveDirection.UP
                GameObject.MoveDirection.LEFT -> GameObject.MoveDirection.UP_LEFT
                GameObject.MoveDirection.DOWN_LEFT -> GameObject.MoveDirection.LEFT
                GameObject.MoveDirection.DOWN -> GameObject.MoveDirection.DOWN_LEFT
                GameObject.MoveDirection.DOWN_RIGHT -> GameObject.MoveDirection.DOWN
            }

    /**
     * Метод рисования всего объекта
     */
    override fun draw(batch: SpriteBatch, x: Float, y: Float) {
        val bodyTimePassedSinceStart = System.currentTimeMillis() - bodyStartTime
        val legsTimePassedSinceStart = System.currentTimeMillis() - legsStartTime

        val bodyFrames = bodyAnimation.data[getViewMoveDirection(moveDirection)]!![weaponType]!!
        if (bodyFrames.isNotEmpty()) {
            val bodyTime = when (true) {
                bodyAnimation.isRepeatable -> bodyTimePassedSinceStart % bodyAnimation.duration
                else -> Math.min(bodyTimePassedSinceStart, bodyAnimation.duration - 1L)
            }
            val bodyStartFrame = bodyFrames[(bodyTime * (bodyFrames.size - 1) / bodyAnimation.duration).toInt()]
            val bodyEndFrame = bodyFrames[Math.min((bodyTime * (bodyFrames.size - 1) / bodyAnimation.duration).toInt() + 1, bodyFrames.size - 1)]
            val bodyInterval = bodyAnimation.duration / (bodyFrames.size - 1f)
            val bodyProgress = bodyTime % bodyInterval / bodyInterval
            val bodyPoint: MutablePoint

            val legsFrames = legsAnimation.data[getViewMoveDirection(moveDirection)]!![weaponType]!!
            val legsTime: Long
            val legsStartFrame: Frame
            val legsEndFrame: Frame
            val legsInterval: Float
            val legsProgress: Float
            if (legsFrames.isNotEmpty()) {
                legsTime = when (true) {
                    legsAnimation.isRepeatable -> legsTimePassedSinceStart % legsAnimation.duration
                    else -> Math.min(legsTimePassedSinceStart, legsAnimation.duration - 1L)
                }
                legsStartFrame = legsFrames[(legsTime * (legsFrames.size - 1) / legsAnimation.duration).toInt()]
                legsEndFrame = legsFrames[Math.min((legsTime * (legsFrames.size - 1) / legsAnimation.duration).toInt() + 1, legsFrames.size - 1)]
                legsInterval = legsAnimation.duration / (legsFrames.size - 1f)
                legsProgress = legsTime % legsInterval / legsInterval
                bodyPoint = getBodyPoint(legsStartFrame, legsEndFrame, legsProgress)
            }
            else {
                legsStartFrame = Frame()
                legsEndFrame = Frame()
                legsProgress = 0f
                bodyPoint = MutablePoint(0f, 0f)
            }
            val bodyX = x + bodyPoint.x
            val bodyY = y - bodyPoint.y

            for (bodyLayerIndex in bodyStartFrame.layers.indices) {
                val startLayer = bodyStartFrame.layers[bodyLayerIndex]
                val endLayer = bodyEndFrame.layers[bodyLayerIndex]
                val layerName = startLayer.getName()
                when (layerName) {
                    "leftleg" -> {
                        val topIndex = findLayer(legsStartFrame, "leftlegtop")
                        val bottomIndex = findLayer(legsStartFrame, "leftlegbottom")
                        if (topIndex < bottomIndex) {
                            if (topIndex != -1) {
                                drawLayer(batch, x, y, "legtop", legsStartFrame.layers[topIndex], legsEndFrame.layers[topIndex], legsProgress)
                            }
                            drawLayer(batch, x, y, "legbottom", legsStartFrame.layers[bottomIndex], legsEndFrame.layers[bottomIndex], legsProgress)
                        }
                        else if (bottomIndex < topIndex) {
                            if (bottomIndex != -1) {
                                drawLayer(batch, x, y, "legbottom", legsStartFrame.layers[bottomIndex], legsEndFrame.layers[bottomIndex], legsProgress)
                            }
                            drawLayer(batch, x, y, "legtop", legsStartFrame.layers[topIndex], legsEndFrame.layers[topIndex], legsProgress)
                        }
                    }
                    "rightleg" -> {
                        val topIndex = findLayer(legsStartFrame, "rightlegtop")
                        val bottomIndex = findLayer(legsStartFrame, "rightlegbottom")
                        if (topIndex < bottomIndex) {
                            if (topIndex != -1) {
                                drawLayer(batch, x, y, "legtop", legsStartFrame.layers[topIndex], legsEndFrame.layers[topIndex], legsProgress)
                            }
                            drawLayer(batch, x, y, "legbottom", legsStartFrame.layers[bottomIndex], legsEndFrame.layers[bottomIndex], legsProgress)
                        }
                        else if (bottomIndex < topIndex) {
                            if (bottomIndex != -1) {
                                drawLayer(batch, x, y, "legbottom", legsStartFrame.layers[bottomIndex], legsEndFrame.layers[bottomIndex], legsProgress)
                            }
                            drawLayer(batch, x, y, "legtop", legsStartFrame.layers[topIndex], legsEndFrame.layers[topIndex], legsProgress)
                        }
                    }
                    else -> drawBodyLayer(batch, bodyX, bodyY, startLayer, endLayer, bodyProgress)
                }
            }
        }
    }

    /**
     * Просматривает слои кадра и возвращает координаты центра слоя bodypoint
     * Если такого слоя нет, возвращает (0f, 0f)
     */
    private fun getBodyPoint(startFrame: Frame, endFrame : Frame, progress: Float) : MutablePoint {
        val startLayerIndex = findLayer(startFrame, "bodypoint")
        val endLayerIndex = findLayer(endFrame, "bodypoint")
        if (startLayerIndex != -1 && endLayerIndex != -1) {
            return curValue(startFrame.layers[startLayerIndex].getPosition(),
                    endFrame.layers[endLayerIndex].getPosition(), progress)
        }
        return MutablePoint(0f, 0f)
    }

    /**
     * Отрисовывает слой изображения (среднее состояние слоя между startLayer и endLayer)
     */
    private fun drawLayer(batch: SpriteBatch, x: Float, y: Float, layerName: String, startLayer: Layer, endLayer : Layer, progress: Float) {
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
        batch.draw(textures[layerName]!!.getTexture(),
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
     * Отрисовывает слой изображения с проверкой имени слоя на некоторые специальные значения
     * (например, bodypoint не будет отрисован)
     */
    private fun drawBodyLayer(batch: SpriteBatch, bodyX: Float, bodyY: Float, startLayer: Layer, endLayer : Layer, progress: Float) {
        var layerName = startLayer.getName()
        if ((layerName == "leftleg") or (layerName == "rightleg") or (layerName == "bodypoint")) {
            return
        }
        layerName = when (true) {
            (layerName == "lefthandtop") or (layerName == "righthandtop") -> "handtop"
            (layerName == "lefthandbottom") or (layerName == "righthandbottom") -> "handbottom"
            (layerName == "leftlegtop") or (layerName == "rightlegtop") -> "legtop"
            (layerName == "leftlegbottom") or (layerName == "rightlegbottom") -> "legbottom"
            (layerName == "onehandedright") or (layerName == "twohanded") or (layerName == "bow") or
                    (layerName == "staff") -> "weapon1"
            (layerName == "onehandedleft") or (layerName == "shield") -> "weapon2"
            else -> layerName
        }
        drawLayer(batch, bodyX, bodyY, layerName, startLayer, endLayer, progress)
    }

    /**
     * Находит слой в кадре по названию и возвращает его номер
     * @return Индекс слоя с данным названием (если он присутствует в кадре) или -1 иначе
     */
    private fun findLayer(frame : Frame, layerName: String) : Int {
        for (layerIndex in frame.layers.indices) {
            if (frame.layers[layerIndex].getName() == layerName) {
                return layerIndex
            }
        }
        return -1
    }


}
