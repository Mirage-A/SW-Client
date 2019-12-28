package com.mirage.ui.fragments.inventory

import com.mirage.core.datastructures.Point
import com.mirage.core.game.objects.properties.*
import com.mirage.core.preferences.Prefs
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.ui.widgets.Widget


internal class InventoryFragment(virtualScreen: VirtualScreen, onClose: () -> Unit = {}) : Widget {

    private val inventoryState = InventoryState()
    private val subWidgets = InventoryWidgets(virtualScreen).apply {
        initializeSizeUpdaters()
        initializeListeners(inventoryState, onClose)
    }

    fun open() {
        with (inventoryState) {
            initialEquipment = Prefs.profile.currentEquipment
            equipment = initialEquipment.copy()
            selectedSlot = null
        }
        with (subWidgets) {
            pageNavigator.isVisible = false
            itemBtns.forEach { it.isVisible = false }
            selectedSlotLabel.text = "Inventory"
            fullDataLabel.text = PlayerAttributes(inventoryState.equipment).toInventoryInfoString()
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
