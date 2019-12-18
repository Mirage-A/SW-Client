package com.mirage.ui.inventory

import com.mirage.ui.widgets.Button
import com.mirage.ui.widgets.CompositeWidget
import com.mirage.ui.widgets.ImageWidget
import com.mirage.ui.widgets.Widget
import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.game.objects.properties.Equipment
import com.mirage.utils.preferences.Prefs
import com.mirage.utils.virtualscreen.VirtualScreen

private const val saveCancelBtnHeight = 80f
private const val saveCancelBtnMargin = 40f
private const val centerColumnWidthPart = 0.25f
private const val equipmentPartIconSize = 196f
private const val equipmentPartIconBorderSize = 8f
private const val equipmentPartBorderedBtnSize = equipmentPartIconSize + 2f * equipmentPartIconBorderSize
private const val equipmentBtnMargin = 16f
private const val itemBtnSize = 128f
private const val leftBackgroundTextureWidth = 900f


class InventoryWindow(virtualScreen: VirtualScreen, onClose: () -> Unit) : Widget {

    private var initialEquipment: Equipment = Prefs.profile.currentEquipment

    private var equipment: Equipment = initialEquipment.copy()

    val centerBackground = ImageWidget("ui/inventory/inventory-center-background") {
        w, h -> Rectangle(0f, 0f, w * centerColumnWidthPart, h)
    }

    val saveBtn = Button(
            "ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Save", 30f),
            {
                w, h -> Rectangle(0f, - h / 2f + saveCancelBtnMargin + saveCancelBtnHeight * 1.5f,
                    w * centerColumnWidthPart - 2f * saveCancelBtnMargin, saveCancelBtnHeight)
            },
            {
                isVisible = false
                Prefs.profile.currentEquipment = equipment
                Prefs.saveCurrentProfile()
                onClose()
            }
    )

    val cancelBtn = Button(
            "ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Cancel", 30f),
            {
                w, h -> Rectangle(0f, - h / 2f + saveCancelBtnMargin + saveCancelBtnHeight / 2f,
                    w * centerColumnWidthPart - 2f * saveCancelBtnMargin, saveCancelBtnHeight)
            },
            {
                isVisible = false
                onClose()
            }
    )

    val centerColumn = CompositeWidget(saveBtn, cancelBtn, centerBackground)

    val leftBackground = ImageWidget("ui/inventory/left-background") {
        w, h -> Rectangle(- w * centerColumnWidthPart / 2f - (w / 2f - w * centerColumnWidthPart / 2f) / 2f, 0f, leftBackgroundTextureWidth, h)
    }

    private fun getArmorX(virtualWidth: Float): Float =
            - virtualWidth / 2f + equipmentBtnMargin + equipmentPartBorderedBtnSize / 2f

    private fun getWeaponX(virtualWidth: Float): Float =
            - (virtualWidth * centerColumnWidthPart / 2f) - equipmentBtnMargin - equipmentPartBorderedBtnSize / 2f

    private fun getHelmetIconTexture(): String = equipment.helmet
    private fun getChestIconTexture(): String = equipment.chest
    private fun getCloakIconTexture(): String = equipment.cloak
    private fun getLegsIconTexture(): String = equipment.legs
    private fun getMainHandIconTexture(): String = equipment.rightWeapon
    private fun getOffHandIconTexture(): String = equipment.leftWeapon

    val leftColumn = CompositeWidget(leftBackground)

    val rightColumn = CompositeWidget()

    val compositeWidget = CompositeWidget(centerColumn, rightColumn, leftColumn)

    fun open() {
        //TODO
        initialEquipment = Prefs.profile.currentEquipment
        equipment = initialEquipment.copy()
        isVisible = true
    }

    override var isVisible: Boolean = false

    override fun resize(virtualWidth: Float, virtualHeight: Float) =
            compositeWidget.resize(virtualWidth, virtualHeight)

    override fun touchUp(virtualPoint: Point): Boolean =
            if (isVisible) compositeWidget.touchUp(virtualPoint) else false

    override fun touchDown(virtualPoint: Point): Boolean =
            if (isVisible) compositeWidget.touchDown(virtualPoint) else false

    override fun mouseMoved(virtualPoint: Point) {
        if (isVisible) compositeWidget.mouseMoved(virtualPoint)
    }

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) compositeWidget.draw(virtualScreen)
    }
}