package com.mirage.gameview.drawers.templates

import com.mirage.gameview.drawers.DrawerTemplate
import com.mirage.gameview.drawers.animation.*
import com.mirage.utils.Log
import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.game.objects.properties.Equipment
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.game.objects.properties.WeaponType
import com.mirage.utils.virtualscreen.VirtualScreen

class HumanoidDrawerTemplate(val equipment: Equipment) : DrawerTemplate {

    private val weaponType: WeaponType = equipment.weaponType

    private val headTextures: Map<MoveDirection, String> = HashMap<MoveDirection, String>().apply {
        MoveDirection.values().forEach {
            this[it] = "equipment/head/${equipment.helmet}/${it.fromSceneToView()}"
        }
    }
    private val bodyTexture = "equipment/body/${equipment.chest}"
    private val cloakTexture = "equipment/cloak/${equipment.cloak}"
    private val handBottomTexture = "equipment/handbottom/${equipment.gloves}"
    private val handTopTexture = "equipment/handtop/${equipment.chest}"
    private val legBottomTexture = "equipment/legbottom/${equipment.legs}"
    private val legTopTexture = "equipment/legtop/${equipment.legs}"
    private val neckTexture = "equipment/neck/${equipment.chest}"
    private val rightWeaponFolder : String? = when (equipment.weaponType) {
        WeaponType.UNARMED -> null
        WeaponType.ONE_HANDED -> "onehanded"
        WeaponType.ONE_HANDED_AND_SHIELD -> "onehanded"
        WeaponType.DUAL -> "onehanded"
        WeaponType.TWO_HANDED -> "twohanded"
        WeaponType.BOW -> "bow"
        WeaponType.STAFF -> "staff"
    }
    private val rightWeaponTexture =
            if (rightWeaponFolder != null) "equipment/$rightWeaponFolder/${equipment.rightWeapon}"
            else "null"
    private val leftWeaponFolder : String? = when(equipment.weaponType) {
        WeaponType.UNARMED -> null
        WeaponType.ONE_HANDED -> null
        WeaponType.ONE_HANDED_AND_SHIELD -> "shield"
        WeaponType.DUAL -> "onehanded"
        WeaponType.TWO_HANDED -> null
        WeaponType.BOW -> null
        WeaponType.STAFF -> null
    }
    private val leftWeaponTexture =
            if (leftWeaponFolder != null) "equipment/$leftWeaponFolder/${equipment.leftWeapon}"
            else "null"


    override fun draw(virtualScreen: VirtualScreen, x: Float, y: Float, isOpaque: Boolean, action: String, actionTimePassedMillis: Long, isMoving: Boolean, movingTimePassedMillis: Long, moveDirection: MoveDirection) {
        if (!isOpaque) return
        val bodyAnimation = AnimationLoader.getBodyAnimation(action)
        val legsAnimation = AnimationLoader.getLegsAnimation(if (isMoving) "running" else "idle")

        val bodyFrames : List<Animation.Frame> = bodyAnimation.data[moveDirection.fromSceneToView()]?.get(weaponType) ?: run {
            Log.e("Error while loading body animation (moveDirection=$moveDirection weaponType=$weaponType action=$action)")
            return
        }
        val legsFrames : List<Animation.Frame> = legsAnimation.data[moveDirection.fromSceneToView()]?.get(weaponType) ?: run {
            Log.e("Error while loading legs animation (moveDirection=$moveDirection weaponType=$weaponType action=$action)")
            return
        }
        if (bodyFrames.isEmpty()) return
        if (legsFrames.isEmpty()) return

        val bodyTime = getAnimationCurrentTime(bodyAnimation, actionTimePassedMillis)
        val bodyStartFrame = getStartFrame(bodyFrames, bodyAnimation.duration, bodyTime)
        val bodyEndFrame = getEndFrame(bodyFrames, bodyAnimation.duration, bodyTime)
        val bodyProgress = getAnimationProgress(bodyFrames, bodyAnimation.duration, bodyTime)

        val legsTime = getAnimationCurrentTime(legsAnimation, movingTimePassedMillis)
        val legsStartFrame = getStartFrame(legsFrames, legsAnimation.duration, legsTime)
        val legsEndFrame = getEndFrame(legsFrames, legsAnimation.duration, legsTime)
        val legsProgress = getAnimationProgress(legsFrames, legsAnimation.duration, legsTime)

        val bodyPoint = getBodyPoint(legsStartFrame, legsEndFrame, legsProgress)
        val bodyX = x + bodyPoint.x
        val bodyY = y - bodyPoint.y

        for (bodyLayerIndex in 0 until bodyStartFrame.layers.size) {
            val startLayer = bodyStartFrame.layers[bodyLayerIndex]
            val endLayer = bodyEndFrame.layers[bodyLayerIndex]
            val layerName = startLayer.getName()
            if (layerName == "leftleg" || layerName == "rightleg") {
                val topIndex = findLayer(legsStartFrame, "${layerName}top")
                val bottomIndex = findLayer(legsStartFrame, "${layerName}bottom")
                if (topIndex < bottomIndex) {
                    if (topIndex != -1) {
                        drawLayer(virtualScreen, legTopTexture, x, y, legsStartFrame.layers[topIndex], legsEndFrame.layers[topIndex], legsProgress)
                    }
                    drawLayer(virtualScreen, legBottomTexture, x, y, legsStartFrame.layers[bottomIndex], legsEndFrame.layers[bottomIndex], legsProgress)
                } else if (bottomIndex < topIndex) {
                    if (bottomIndex != -1) {
                        drawLayer(virtualScreen, legBottomTexture, x, y, legsStartFrame.layers[bottomIndex], legsEndFrame.layers[bottomIndex], legsProgress)
                    }
                    drawLayer(virtualScreen, legTopTexture, x, y, legsStartFrame.layers[topIndex], legsEndFrame.layers[topIndex], legsProgress)
                }
            }
            else drawBodyLayer(virtualScreen, bodyX, bodyY, startLayer, endLayer, bodyProgress, moveDirection)

        }
    }


    /**
     * Просматривает слои кадра и возвращает координаты центра слоя bodypoint
     * Если такого слоя нет, возвращает (0f, 0f)
     */
    private fun getBodyPoint(startFrame: Animation.Frame, endFrame : Animation.Frame, progress: Float) : Point {
        val startLayerIndex = findLayer(startFrame, "bodypoint")
        val endLayerIndex = findLayer(endFrame, "bodypoint")
        if (startLayerIndex != -1 && endLayerIndex != -1) {
            return curValue(startFrame.layers[startLayerIndex].getPosition(),
                    endFrame.layers[endLayerIndex].getPosition(), progress)
        }
        return Point(0f, 0f)
    }


    /**
     * Отрисовывает слой изображения с проверкой имени слоя на некоторые специальные значения
     * (например, bodypoint не будет отрисован)
     */
    private fun drawBodyLayer(virtualScreen: VirtualScreen, bodyX: Float, bodyY: Float, startLayer: Animation.Layer, endLayer : Animation.Layer, progress: Float, moveDirection: MoveDirection) {
        val layerName = startLayer.getName()
        if ((layerName == "leftleg") or (layerName == "rightleg") or (layerName == "bodypoint")) {
            return
        }
        val texture : String = when (layerName) {
            "lefthandtop", "righthandtop" -> handTopTexture
            "lefthandbottom", "righthandbottom" -> handBottomTexture
            "leftlegtop", "rightlegtop" -> legTopTexture
            "leftlegbottom", "rightlegbottom" -> legBottomTexture
            "onehandedright", "twohanded", "bow", "staff" -> rightWeaponTexture
            "onehandedleft", "shield" -> leftWeaponTexture
            "cloak" -> cloakTexture
            "body" -> bodyTexture
            "neck" -> neckTexture
            "head" -> headTextures[moveDirection] ?: "null"
            else -> startLayer.imageName.substring(0, startLayer.imageName.length - 4)
        }
        drawLayer(virtualScreen, texture, bodyX, bodyY, startLayer, endLayer, progress)
    }

    override val hitBox: Rectangle
        get() = Rectangle(0f, 64f, 128f, 128f)

}