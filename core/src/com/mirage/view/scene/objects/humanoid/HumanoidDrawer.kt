package com.mirage.view.scene.objects.humanoid

import com.mirage.view.TextureLoader
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.model.scene.Point
import com.mirage.view.animation.*
import com.mirage.view.scene.objects.AnimatedObjectDrawer
import java.util.HashMap


/**
 * Класс для работы со скелетной анимацией гуманоидов
 * (анимация остальных существ задается покадрово классом SpriteAnimation)
 */
class HumanoidDrawer : AnimatedObjectDrawer {
    /**
     * Действие, которое анимируется (ожидание, бег, атака и т.д.)
     */
    var bodyAction = BodyAction.IDLE
    var legsAction = LegsAction.IDLE
    /**
     * Непосредственно анимации
     */
    var bodyAnimation = Animations.getBodyAnimation(BodyAction.IDLE)
    var legsAnimation = Animations.getLegsAnimation(LegsAction.IDLE)
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

    override fun draw(batch: SpriteBatch, x: Float, y: Float, timePassedSinceStart: Long) {
        val bodyTimePassedSinceStart = timePassedSinceStart - bodyStartTime
        val legsTimePassedSinceStart = timePassedSinceStart - legsStartTime
        val bodyPoint = getBodyPoint(legsTimePassedSinceStart)
        val bodyX = x + bodyPoint.x
        val bodyY = y + bodyPoint.y

    }
    private fun getBodyPoint(legsTimePassedSinceStart: Long) : Point {

        return Point(0f, 0f)
    }

    /**
     * Просматривает слои кадра и возвращает координаты центра слоя bodypoint
     * Если такого слоя нет, возвращает (0f, 0f)
     */
    private fun getBodyPoint(frame : Frame) : Pair<Float, Float> {
        for (layer in frame.layers) {
            if (layer.getName() == "bodypoint") {
                return Pair(layer.x, -layer.y)
            }
        }
        return Pair(0f, 0f)
    }

    private fun drawLeftLeg(batch: SpriteBatch, x: Float, y: Float, legsTimePassedSinceStart: Long) {

    }
    private fun drawRightLeg(batch: SpriteBatch, x: Float, y: Float, legsTimePassedSinceStart: Long) {

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
}
