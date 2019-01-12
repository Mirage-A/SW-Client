package com.mirage.view.scene.objects.humanoid

import com.mirage.view.TextureLoader
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.model.scene.Point
import com.mirage.view.animation.*
import com.mirage.view.scene.objects.ObjectDrawer
import java.util.HashMap


/**
 * Класс для работы со скелетной анимацией гуманоидов
 * (анимация остальных существ задается покадрово классом SpriteAnimation)
 */
class HumanoidDrawer : ObjectDrawer {
    /**
     * Действие, которое анимируется (ожидание, бег, атака и т.д.)
     */
    private var bodyAction = BodyAction.IDLE
    private var legsAction = LegsAction.IDLE
    /**
     * Непосредственно анимации
     */
    private var bodyAnimation = Animations.getBodyAnimation(BodyAction.IDLE)
    private var legsAnimation = Animations.getLegsAnimation(LegsAction.IDLE)
    /**
     * Направление движения
     */
    var moveDirection: MoveDirection = MoveDirection.RIGHT
    /**
     * Тип оружия гуманоида (одноручное, двуручное, два одноручных, одноручное и щит, лук и т.д.)
     */
    var weaponType: WeaponType = WeaponType.UNARMED

    /**
     * Словарь из текстур экипировки данного гуманоида
     * Должен содержать ключи head[RIGHT, DOWN, etc], body, handtop, handbottom, legtop, legbottom, cloak, weapon1, weapon2
     */
    var textures: MutableMap<String, AnimatedTexture>

    /**
     * Время начала анимации body
     */
    var bodyStartTime = 0L

    /**
     * Время начала анимации legs
     */
    var legsStartTime = 0L

    /**
     * Буфер направления движения, необходим для плавности поворота персонажа
     */
    var bufferedMoveDirection = MoveDirection.RIGHT

    /**
     * Время последнего изменения направления движения
     */
    var lastMoveDirectionUpdateTime = 0L

    constructor() {
        textures = HashMap()
        for (md in MoveDirection.values()) {
            textures["head" + md.toString()] = StaticTexture(TextureLoader.load("equipment/head/0000" + md.toString() + ".png"))
        }
        textures["body"] = StaticTexture(TextureLoader.load("equipment/body/0000.png"))
        textures["handtop"] = StaticTexture(TextureLoader.load("equipment/handtop/0000.png"))
        textures["handbottom"] = StaticTexture(TextureLoader.load("equipment/handbottom/0000.png"))
        textures["legtop"] = StaticTexture(TextureLoader.load("equipment/legtop/0000.png"))
        textures["legbottom"] = StaticTexture(TextureLoader.load("equipment/legbottom/0000.png"))
        textures["cloak"] = StaticTexture(TextureLoader.load("equipment/cloak/0000.png"))
        textures["neck"] = StaticTexture(TextureLoader.load("equipment/neck/0000.png"))
        textures["weapon1"] = StaticTexture(TextureLoader.load("equipment/onehanded/0000.png"))
        textures["weapon2"] = StaticTexture(TextureLoader.load("equipment/onehanded/0000.png"))
    }
    constructor(textures: MutableMap<String, AnimatedTexture>) {
        this.textures = textures
    }

    constructor(textures: MutableMap<String, AnimatedTexture>, bodyAction: BodyAction, legsAction: LegsAction, moveDirection: MoveDirection, weaponType: WeaponType) {
        this.textures = textures
        this.bodyAction = bodyAction
        this.legsAction = legsAction
        this.moveDirection = moveDirection
        this.weaponType = weaponType
    }


    private fun curValue(startValue: Float, endValue : Float, progress : Float) : Float {
        return startValue + (endValue - startValue) * progress
    }

    private fun curValue(startValue: Int, endValue : Int, progress : Float) : Float {
        return startValue + (endValue - startValue) * progress
    }

    private fun curValue(startPoint: Point, endPoint: Point, progress: Float) : Point {
        return Point(curValue(startPoint.x, endPoint.x, progress), curValue(startPoint.y, endPoint.y, progress))
    }

    override fun draw(batch: SpriteBatch, x: Float, y: Float) {
        val time = System.currentTimeMillis()
        val bodyTimePassedSinceStart = System.currentTimeMillis() - bodyStartTime
        val legsTimePassedSinceStart = System.currentTimeMillis() - legsStartTime

        val bodyFrames = bodyAnimation.data[moveDirection]!![weaponType]!!
        if (bodyFrames.isNotEmpty()) {
            val bodyTime = when (true) {
                bodyAnimation.isRepeatable -> bodyTimePassedSinceStart % bodyAnimation.duration
                else -> Math.min(bodyTimePassedSinceStart, bodyAnimation.duration - 1L)
            }
            val bodyStartFrame = bodyFrames[(bodyTime * (bodyFrames.size - 1) / bodyAnimation.duration).toInt()]
            val bodyEndFrame = bodyFrames[Math.min((bodyTime * (bodyFrames.size - 1) / bodyAnimation.duration).toInt() + 1, bodyFrames.size - 1)]
            val bodyInterval = bodyAnimation.duration / (bodyFrames.size - 1f)
            val bodyProgress = bodyTime % bodyInterval / bodyInterval
            val bodyPoint: Point

            val legsFrames = legsAnimation.data[moveDirection]!![weaponType]!!
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
                bodyPoint = Point(0f, 0f)
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
                                drawLayerOnBatch(batch, x, y, "legtop", legsStartFrame.layers[topIndex], legsEndFrame.layers[topIndex], legsProgress)
                            }
                            drawLayerOnBatch(batch, x, y, "legbottom", legsStartFrame.layers[bottomIndex], legsEndFrame.layers[bottomIndex], legsProgress)
                        }
                        else if (bottomIndex < topIndex) {
                            if (bottomIndex != -1) {
                                drawLayerOnBatch(batch, x, y, "legbottom", legsStartFrame.layers[bottomIndex], legsEndFrame.layers[bottomIndex], legsProgress)
                            }
                            drawLayerOnBatch(batch, x, y, "legtop", legsStartFrame.layers[topIndex], legsEndFrame.layers[topIndex], legsProgress)
                        }
                    }
                    "rightleg" -> {
                        val topIndex = findLayer(legsStartFrame, "rightlegtop")
                        val bottomIndex = findLayer(legsStartFrame, "rightlegbottom")
                        if (topIndex < bottomIndex) {
                            if (topIndex != -1) {
                                drawLayerOnBatch(batch, x, y, "legtop", legsStartFrame.layers[topIndex], legsEndFrame.layers[topIndex], legsProgress)
                            }
                            drawLayerOnBatch(batch, x, y, "legbottom", legsStartFrame.layers[bottomIndex], legsEndFrame.layers[bottomIndex], legsProgress)
                        }
                        else if (bottomIndex < topIndex) {
                            if (bottomIndex != -1) {
                                drawLayerOnBatch(batch, x, y, "legbottom", legsStartFrame.layers[bottomIndex], legsEndFrame.layers[bottomIndex], legsProgress)
                            }
                            drawLayerOnBatch(batch, x, y, "legtop", legsStartFrame.layers[topIndex], legsEndFrame.layers[topIndex], legsProgress)
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
    private fun getBodyPoint(startFrame: Frame, endFrame : Frame, progress: Float) : Point {
        val startLayerIndex = findLayer(startFrame, "bodypoint")
        val endLayerIndex = findLayer(endFrame, "bodypoint")
        if (startLayerIndex != -1 && endLayerIndex != -1) {
            return curValue(startFrame.layers[startLayerIndex].getPosition(),
                    endFrame.layers[endLayerIndex].getPosition(), progress)
        }
        return Point(0f, 0f)
    }

    private fun drawLayer(batch: SpriteBatch, x: Float, y: Float, startLayer: Layer, endLayer : Layer, progress: Float) {
        var layerName = startLayer.getName()
        if ((layerName == "leftleg") or (layerName == "rightleg") or (layerName == "bodypoint")) {
            return
        }
        layerName = when (true) {
            (layerName == "leftlegtop") or (layerName == "rightlegtop") -> "legtop"
            (layerName == "leftlegbottom") or (layerName == "rightlegbottom") -> "legbottom"
            (layerName == "onehandedright") or (layerName == "twohanded") or (layerName == "bow") or
                    (layerName == "staff") -> "weapon1"
            (layerName == "onehandedleft") or (layerName == "shield") -> "weapon2"
            else -> layerName
        }
        drawLayerOnBatch(batch, x, y, layerName, startLayer, endLayer, progress)
    }

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
        drawLayerOnBatch(batch, bodyX, bodyY, layerName, startLayer, endLayer, progress)
    }

    private fun drawLayerOnBatch(batch: SpriteBatch, x: Float, y: Float, layerName: String, startLayer: Layer, endLayer : Layer, progress: Float) {
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

    fun setBodyAction(action: BodyAction) {
        if (action != bodyAction) {
            bodyAction = action
            bodyStartTime = System.currentTimeMillis()
            bodyAnimation = Animations.getBodyAnimation(action)
        }
    }

    fun setLegsAction(action: LegsAction) {
        if (action != legsAction) {
            legsAction = action
            legsStartTime = System.currentTimeMillis()
            legsAnimation = Animations.getLegsAnimation(action)
        }
    }

}
