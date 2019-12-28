package com.mirage.ui.widgets

import com.mirage.core.game.objects.properties.EquipmentSlot
import com.mirage.core.utils.Point
import com.mirage.core.utils.Rectangle
import com.mirage.core.virtualscreen.VirtualScreen

/** Button displaying a piece of equipment */
internal class EquipmentButton(
        var textureName: String = "ui/main-menu-btn",
        var highlightedTextureName: String =
                if (textureName == "ui/main-menu-btn") "ui/main-menu-btn-highlighted" else textureName,
        var pressedTextureName: String =
                if (highlightedTextureName == "ui/main-menu-btn-highlighted") "ui/main-menu-btn-pressed" else highlightedTextureName,
        var sizeUpdater: SizeUpdater? = null,
        var onPressed: () -> Unit = {},
        var borderSize: Float = 0f,
        var borderTextureName: String = "ui/btn-border",
        var equipmentSlot: EquipmentSlot = EquipmentSlot.MAIN_HAND,
        var itemName: String = "null"
) : Widget {

    override var isVisible = true

    private var isHighlighted = false
    private var isPressed = false
    private var keyPressed = false

    private var rect: Rectangle = Rectangle()

    private val innerRect: Rectangle
        get() = rect.innerRect(borderSize)

    override fun resize(virtualWidth: Float, virtualHeight: Float) {
        rect = sizeUpdater?.invoke(virtualWidth, virtualHeight) ?: Rectangle()
    }

    override fun touchUp(virtualPoint: Point): Boolean {
        if (!isVisible || !isPressed) return false
        isPressed = false
        return if (rect.contains(virtualPoint)) {
            onPressed()
            true
        } else false
    }

    override fun touchDown(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        isPressed = rect.contains(virtualPoint)
        return isPressed
    }

    override fun mouseMoved(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        isHighlighted = rect.contains(virtualPoint)
        return isHighlighted
    }

    override fun touchDragged(virtualPoint: Point): Boolean {
        if (!isVisible) return false
        isHighlighted = rect.contains(virtualPoint)
        return isHighlighted
    }

    override fun draw(virtualScreen: VirtualScreen) {
        if (!isVisible) return
        if (borderSize != 0f) virtualScreen.draw(borderTextureName, rect)
        val textureName = when {
            isPressed || keyPressed -> pressedTextureName
            isHighlighted -> highlightedTextureName
            else -> textureName
        }
        virtualScreen.draw(textureName, innerRect)
        when (equipmentSlot) {
            EquipmentSlot.HELMET -> ::drawHelmetIcon
            EquipmentSlot.CHEST -> ::drawChestIcon
            EquipmentSlot.LEGGINGS -> ::drawLegsIcon
            EquipmentSlot.CLOAK -> ::drawCloakIcon
            EquipmentSlot.MAIN_HAND, EquipmentSlot.OFF_HAND -> ::drawWeaponIcon
        }.invoke(virtualScreen, itemName, innerRect)
    }


    private fun drawHelmetIcon(virtualScreen: VirtualScreen, helmet: String, rect: Rectangle) {
        if (helmet == "null") drawNoneIcon(virtualScreen, helmet, rect)
        else {
            val helmetTexture = "../equipment/head/$helmet/DOWN"
            virtualScreen.draw(helmetTexture, rect)
        }
    }

    private fun drawChestIcon(virtualScreen: VirtualScreen, chest: String, rect: Rectangle) {
        if (chest == "null") drawNoneIcon(virtualScreen, chest, rect)
        else {
            val bodyTexture = "../equipment/body/$chest/body"
            val handTopTexture = "../equipment/body/$chest/handtop"
            val handBottomTexture = "../equipment/body/$chest/handbottom"
            val neckTexture = "../equipment/body/$chest/neck"
            //TODO
            virtualScreen.draw(bodyTexture, rect)
        }
    }

    private fun drawLegsIcon(virtualScreen: VirtualScreen, legs: String, rect: Rectangle) {
        if (legs == "null") drawNoneIcon(virtualScreen, legs, rect)
        else {
            val legTopTexture = "../equipment/legs/$legs/legtop"
            val legBottomTexture = "../equipment/legs/$legs/legbottom"
            //TODO
            virtualScreen.draw(legBottomTexture, rect)
        }
    }

    private fun drawCloakIcon(virtualScreen: VirtualScreen, cloak: String, rect: Rectangle) {
        if (cloak == "null") drawNoneIcon(virtualScreen, cloak, rect)
        else {
            val cloakTexture = "../equipment/cloak/$cloak/cloak"
            //TODO
            virtualScreen.draw(cloakTexture, rect)
        }
    }

    private fun drawWeaponIcon(virtualScreen: VirtualScreen, weapon: String, rect: Rectangle) {
        if (weapon == "null") drawNoneIcon(virtualScreen, weapon, rect)
        else {
            val weaponTexture = "../equipment/weapon/$weapon/weapon"
            //TODO
            virtualScreen.draw(weaponTexture, rect)
        }
    }

    private fun drawNoneIcon(virtualScreen: VirtualScreen, mockString: String = "none", rect: Rectangle) {
        virtualScreen.draw("ui/inventory/no-selection", rect)
    }

}