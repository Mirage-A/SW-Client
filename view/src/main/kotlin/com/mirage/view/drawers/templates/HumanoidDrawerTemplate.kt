package com.mirage.view.drawers.templates

import com.mirage.core.utils.Log
import com.mirage.core.utils.Point
import com.mirage.core.utils.Rectangle
import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.game.objects.properties.WeaponType
import com.mirage.core.VirtualScreen
import com.mirage.view.drawers.DrawerTemplate
import com.mirage.view.drawers.animation.*

class HumanoidDrawerTemplate(
        private val animationLoader: AnimationLoader,
        val equipment: Equipment,
        val scale: Float = 1f
) : DrawerTemplate {

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


    override fun draw(virtualScreen: VirtualScreen, x: Float, y: Float, width: Float, height: Float, isOpaque: Boolean, action: String, actionTimePassedMillis: Long, isMoving: Boolean, movingTimePassedMillis: Long, moveDirection: MoveDirection) {
        if (!isOpaque) return
        val bodyAnimation = animationLoader.getBodyAnimation(action)
        val legsAnimation = animationLoader.getLegsAnimation(if (isMoving) "running" else "idle")
        var bodyFrames: List<Animation.Frame> = bodyAnimation.data[moveDirection.fromSceneToView()]?.get(weaponType)
                ?: ArrayList()
        if (bodyFrames.isEmpty()) {
            bodyFrames = bodyAnimation.data[moveDirection.fromSceneToView()]?.get(WeaponType.UNARMED)
                    ?: run {
                        Log.e("Error while loading body animation (moveDirection=$moveDirection weaponType=$weaponType action=$action)")
                        return
                    }
        }
        var legsFrames: List<Animation.Frame> = legsAnimation.data[moveDirection.fromSceneToView()]?.get(weaponType)
                ?: ArrayList()
        if (legsFrames.isEmpty())
            legsFrames = legsAnimation.data[moveDirection.fromSceneToView()]?.get(WeaponType.UNARMED)
                    ?: run {
                        Log.e("Error while loading legs animation (moveDirection=$moveDirection weaponType=$weaponType action=$action)")
                        return
                    }

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
            if (startLayer.isVisible && endLayer.isVisible)
                drawPlayerLayer(virtualScreen, bodyX, bodyY, startLayer, endLayer, bodyProgress, moveDirection)
            if (layerName == "~body") { // Legs are always drawn right after body
                for (legsLayerIndex in 0 until legsStartFrame.layers.size) {
                    val legsStartLayer = legsStartFrame.layers[legsLayerIndex]
                    val legsEndLayer = legsEndFrame.layers[legsLayerIndex]
                    if (legsStartLayer.isVisible && legsEndLayer.isVisible)
                        drawPlayerLayer(virtualScreen, x, y, legsStartLayer, legsEndLayer, legsProgress, moveDirection)
                }
            }

        }
    }


    /** Finds a bodypoint layer on frame and returns its position, or (0f, 0f) if it is absent */
    private fun getBodyPoint(startFrame: Animation.Frame, endFrame: Animation.Frame, progress: Float): Point {
        val startLayerIndex = findLayer(startFrame, "~bodypoint")
        val endLayerIndex = findLayer(endFrame, "~bodypoint")
        if (startLayerIndex != -1 && endLayerIndex != -1) {
            return curValue(startFrame.layers[startLayerIndex].getPosition(),
                    endFrame.layers[endLayerIndex].getPosition(), progress) * scale
        }
        return Point(0f, 0f)
    }


    /** Draws a layer, processing special layers like ~bodypoint */
    private fun drawPlayerLayer(virtualScreen: VirtualScreen, x: Float, y: Float, startLayer: Animation.Layer, endLayer: Animation.Layer, progress: Float, moveDirection: MoveDirection) {
        val layerName = startLayer.imageName
        val texture: String = when {
            layerName.startsWith("~head-") -> {
                val customMoveDirection = MoveDirection.fromString(layerName.substring(6))
                headTextures[customMoveDirection.fromViewToScene()] ?: return
            }
            layerName.startsWith("~") -> when (layerName) {
                "~bodypoint" -> return
                "~legtop" -> legTopTexture
                "~legbottom" -> legBottomTexture
                "~handtop" -> handTopTexture
                "~handbottom" -> handBottomTexture
                "~onehanded", "~mainhand", "~twohanded", "~staff", "~bow" -> rightWeaponTexture
                "~offhand", "~shield" -> leftWeaponTexture
                "~cloak" -> cloakTexture
                "~body" -> bodyTexture
                "~neck" -> neckTexture
                "~head" -> headTextures[moveDirection] ?: return
                else -> return
            }
            else -> layerName
        }
        drawLayer(virtualScreen, texture, x, y, startLayer, endLayer, progress, scale)
    }

    override val hitBox: Rectangle
        get() = Rectangle(0f, 64f, 128f, 128f)

}