package com.mirage.ui.inventory

import com.mirage.view.drawers.templates.HumanoidDrawerTemplate
import com.mirage.ui.widgets.*
import com.mirage.core.Assets
import com.mirage.core.Log
import com.mirage.core.datastructures.Point
import com.mirage.core.datastructures.Rectangle
import com.mirage.core.game.objects.properties.*
import com.mirage.core.preferences.EquipmentSlot
import com.mirage.core.preferences.Prefs
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.ui.widgets.Widget
import kotlin.math.min



internal class InventoryWindow(virtualScreen: VirtualScreen, onClose: () -> Unit) : Widget {

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

    override var isVisible: Boolean = false

    override fun resize(virtualWidth: Float, virtualHeight: Float) =
            subWidgets.rootWidget.resize(virtualWidth, virtualHeight)

    override fun touchUp(virtualPoint: Point): Boolean =
            isVisible && subWidgets.rootWidget.touchUp(virtualPoint)

    override fun touchDown(virtualPoint: Point): Boolean =
            isVisible && subWidgets.rootWidget.touchDown(virtualPoint)

    override fun mouseMoved(virtualPoint: Point): Boolean =
        isVisible && subWidgets.rootWidget.mouseMoved(virtualPoint)

    override fun draw(virtualScreen: VirtualScreen) {
        if (isVisible) subWidgets.rootWidget.draw(virtualScreen)
    }

}
