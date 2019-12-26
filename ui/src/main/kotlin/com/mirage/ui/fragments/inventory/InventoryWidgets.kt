package com.mirage.ui.fragments.inventory

import com.mirage.core.preferences.EquipmentSlot
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.ui.widgets.*
import com.mirage.ui.widgets.Button
import com.mirage.ui.widgets.ConfirmMessage
import com.mirage.ui.widgets.ImageWidget
import com.mirage.ui.widgets.LabelWidget
import com.mirage.ui.widgets.PageNavigator


internal const val itemBtnsCount = 16

/** Components of InventoryWidget */
internal class InventoryWidgets(virtualScreen: VirtualScreen) {

    val centerBackground = ImageWidget("ui/inventory/inventory-center-background")

    val saveBtn = Button(
            boundedLabel = LabelWidget(virtualScreen, "Save", 30f)
    )

    val cancelBtn = Button(
            boundedLabel = LabelWidget(virtualScreen, "Cancel", 30f)
    )

    val fullDataLabel = LabelWidget(virtualScreen, "", 30f)/*TODO */

    val leftBackground = ImageWidget("ui/inventory/left-background")/*TODO */

    val humanoidWidget = HumanoidWidget()

    val equipmentSlots = listOf(
            EquipmentSlot.HELMET, EquipmentSlot.CHEST, EquipmentSlot.LEGGINGS,
            EquipmentSlot.CLOAK, EquipmentSlot.MAIN_HAND, EquipmentSlot.OFF_HAND
    )

    /** Buttons on the left side of inventory. These buttons are used to select an equipment slot to change. */
    val selectSlotBtns = Array(equipmentSlots.size) {
        EquipmentButton(
                textureName = "ui/inventory/left-button-background",
                highlightedTextureName = "ui/inventory/left-button-background-highlighted",
                equipmentSlot = equipmentSlots[it]
        )
    }

    val rightBackground = ImageWidget("ui/inventory/right-background")

    val selectedSlotLabel = LabelWidget(virtualScreen, "Inventory", 42f)

    val leftArrow = Button(textureName = "ui/inventory/left-arrow")

    val rightArrow = Button(textureName = "ui/inventory/right-arrow")

    val pageLabel = LabelWidget(virtualScreen, "", 32f)

    val pageNavigator = PageNavigator(0, 1, leftArrow, rightArrow, pageLabel)

    val itemBtns = Array(itemBtnsCount) {
        EquipmentButton(
                textureName = "ui/inventory/left-button-background",
                highlightedTextureName = "ui/inventory/left-button-background-highlighted"
        )
    }

    val itemMessage = ConfirmMessage(
            virtualScreen, "Title", "description", "Equip", "Cancel", true
    )

    private val leftColumn = CompositeWidget(*selectSlotBtns, humanoidWidget, leftBackground)
    private val centerColumn = CompositeWidget(fullDataLabel, saveBtn, cancelBtn, centerBackground)
    private val rightColumn = CompositeWidget(*itemBtns, pageNavigator, selectedSlotLabel, rightBackground)

    val rootWidget = CompositeWidget(itemMessage, centerColumn, rightColumn, leftColumn)

}