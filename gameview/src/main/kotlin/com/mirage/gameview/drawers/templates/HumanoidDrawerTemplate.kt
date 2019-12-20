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

class HumanoidDrawerTemplate(val equipment: Equipment, val scale: Float = 1f) : DrawerTemplate {

    private val weaponType: WeaponType = equipment.weaponType

    private val headTextures: Map<MoveDirection, String> = HashMap<MoveDirection, String>().apply {
        MoveDirection.values().forEach {
            this[it] = "../equipment/head/${equipment.helmet}/${it.fromSceneToView()}"
        }
    }
    private val bodyTexture = "../equipment/body/${equipment.chest}/body"
    private val cloakTexture = "../equipment/cloak/${equipment.cloak}/cloak"
    private val handBottomTexture = "../equipment/body/${equipment.chest}/handbottom"
    private val handTopTexture = "../equipment/body/${equipment.chest}/handtop"
    private val legBottomTexture = "../equipment/legs/${equipment.legs}/legbottom"
    private val legTopTexture = "../equipment/legs/${equipment.legs}/legtop"
    private val neckTexture = "../equipment/body/${equipment.chest}/neck"
    private val rightWeaponTexture =
            if (equipment.weaponType != WeaponType.UNARMED) "../equipment/weapon/${equipment.mainHand}/weapon"
            else "null"
    private val leftWeaponTexture =
            if (equipment.weaponType in arrayOf(WeaponType.SHIELD, WeaponType.DUAL))
                "../equipment/weapon/${equipment.offHand}/weapon"
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
                        drawLayer(virtualScreen, legTopTexture, x, y, legsStartFrame.layers[topIndex], legsEndFrame.layers[topIndex], legsProgress, scale)
                    }
                    drawLayer(virtualScreen, legBottomTexture, x, y, legsStartFrame.layers[bottomIndex], legsEndFrame.layers[bottomIndex], legsProgress, scale)
                } else if (bottomIndex < topIndex) {
                    if (bottomIndex != -1) {
                        drawLayer(virtualScreen, legBottomTexture, x, y, legsStartFrame.layers[bottomIndex], legsEndFrame.layers[bottomIndex], legsProgress, scale)
                    }
                    drawLayer(virtualScreen, legTopTexture, x, y, legsStartFrame.layers[topIndex], legsEndFrame.layers[topIndex], legsProgress, scale)
                }
            }
            else drawBodyLayer(virtualScreen, bodyX, bodyY, startLayer, endLayer, bodyProgress, moveDirection)

        }
    }


    /** Finds a bodypoint layer on frame and returns its position, or (0f, 0f) if it is absent */
    private fun getBodyPoint(startFrame: Animation.Frame, endFrame : Animation.Frame, progress: Float) : Point {
        val startLayerIndex = findLayer(startFrame, "bodypoint")
        val endLayerIndex = findLayer(endFrame, "bodypoint")
        if (startLayerIndex != -1 && endLayerIndex != -1) {
            return curValue(startFrame.layers[startLayerIndex].getPosition(),
                    endFrame.layers[endLayerIndex].getPosition(), progress) * scale
        }
        return Point(0f, 0f)
    }


    /** Draws a layer, processing special layers like bodypoint */
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
        drawLayer(virtualScreen, texture, bodyX, bodyY, startLayer, endLayer, progress, scale)
    }

    override val hitBox: Rectangle
        get() = Rectangle(0f, 64f, 128f, 128f)

}