package com.mirage.ui.fragments.inventory

import com.mirage.core.utils.Point
import com.mirage.core.game.objects.properties.PlayerAttributes
import com.mirage.core.VirtualScreen
import com.mirage.core.preferences.Preferences
import com.mirage.core.utils.Assets
import com.mirage.core.utils.EquipmentLoader
import com.mirage.ui.widgets.Widget


internal class InventoryFragment(
        virtualScreen: VirtualScreen,
        assets: Assets,
        preferences: Preferences,
        onClose: () -> Unit = {}
) : Widget {

    private val inventoryState = InventoryState(assets, preferences)
    private val subWidgets = InventoryWidgets(virtualScreen, inventoryState).apply {
        initializeSizeUpdaters()
        initializeListeners(inventoryState, onClose)
    }

    fun open() {
        with(inventoryState) {
            initialEquipment = preferences.profile.currentEquipment
            equipment = initialEquipment.copy()
            selectedSlot = null
        }
        with(subWidgets) {
            pageNavigator.isVisible = false
            itemBtns.forEach { it.isVisible = false }
            selectedSlotLabel.text = "Inventory"
            fullDataLabel.text = PlayerAttributes(inventoryState.equipmentLoader, inventoryState.equipment).toInventoryInfoString()
            humanoidWidget.equipment = inventoryState.equipment
        }
        isVisible = true
    }

    override var isVisible: Boolean
        get() = subWidgets.rootWidget.isVisible
        set(value) {
            subWidgets.rootWidget.isVisible = value
        }

    override fun resize(virtualWidth: Float, virtualHeight: Float) =
            subWidgets.rootWidget.resize(virtualWidth, virtualHeight)

    override fun touchUp(virtualPoint: Point, pointer: Int, button: Int): Boolean =
            isVisible && subWidgets.rootWidget.touchUp(virtualPoint, pointer, button)

    override fun touchDown(virtualPoint: Point, pointer: Int, button: Int): Boolean =
            isVisible && subWidgets.rootWidget.touchDown(virtualPoint, pointer, button)

    override fun mouseMoved(virtualPoint: Point): Boolean =
            isVisible && subWidgets.rootWidget.mouseMoved(virtualPoint)

    override fun touchDragged(virtualPoint: Point, pointer: Int): Boolean =
            isVisible && subWidgets.rootWidget.touchDragged(virtualPoint, pointer)

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) subWidgets.rootWidget.draw(virtualScreen)
    }

}
