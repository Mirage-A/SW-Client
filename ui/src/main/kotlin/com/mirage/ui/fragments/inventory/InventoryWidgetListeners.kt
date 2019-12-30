package com.mirage.ui.fragments.inventory

import com.mirage.core.game.objects.properties.EquipmentSlot
import com.mirage.core.utils.GdxAssets
import com.mirage.core.utils.Log
import com.mirage.core.game.objects.properties.PlayerAttributes
import com.mirage.core.game.objects.properties.WeaponType
import com.mirage.core.preferences.GdxPreferences

internal fun InventoryWidgets.initializeListeners(inventoryState: InventoryState, onClose: () -> Unit) {
    saveBtn.onPressed = {
        rootWidget.isVisible = false
        GdxPreferences.profile.currentEquipment = inventoryState.equipment
        GdxPreferences.saveCurrentProfile()
        onClose()
    }
    cancelBtn.onPressed = {
        rootWidget.isVisible = false
        onClose()
    }
    for ((i, slot) in equipmentSlots.withIndex()) {
        selectSlotBtns[i].onPressed = {
            selectEquipmentSlot(inventoryState, slot)
            updateItemBtns(inventoryState)
        }
    }
    itemMessage.setCancelAction { rootWidget.isVisible = false }
    updateSelectSlotBtns(inventoryState)
    updateItemBtns(inventoryState)
}

private fun InventoryWidgets.updateSelectSlotBtns(inventoryState: InventoryState) {
    for ((i, slot) in equipmentSlots.withIndex()) {
        selectSlotBtns[i].equipmentSlot = slot
        selectSlotBtns[i].itemName = inventoryState.equipment.getItemName(slot)
    }
}

private fun InventoryWidgets.updateItemBtns(inventoryState: InventoryState) {
    val items = GdxPreferences.account.availableEquipment[inventoryState.selectedSlot]
    if (items != null) {
        val itemsCount = items.size
        for (i in 0 until itemBtnsCount) {
            val itemIndex = i + itemBtnsCount * pageNavigator.pageIndex
            if (i >= itemsCount) break
            itemBtns[i].equipmentSlot = inventoryState.selectedSlot ?: EquipmentSlot.MAIN_HAND
            itemBtns[i].itemName = items[itemIndex]
        }
    }
}

private fun getSlotName(slot: EquipmentSlot?) = when (slot) {
    null -> "Inventory"
    EquipmentSlot.HELMET -> "Helmet"
    EquipmentSlot.CHEST -> "Chest"
    EquipmentSlot.LEGGINGS -> "Leggings"
    EquipmentSlot.CLOAK -> "Cloak"
    EquipmentSlot.MAIN_HAND -> "Main hand"
    EquipmentSlot.OFF_HAND -> "Off hand"
}

private fun InventoryWidgets.selectEquipmentSlot(inventoryState: InventoryState, slot: EquipmentSlot?) {
    if (slot == null || slot == inventoryState.selectedSlot) {
        inventoryState.selectedSlot = null
        pageNavigator.isVisible = false
        itemBtns.forEach { it.isVisible = false }
        selectedSlotLabel.text = "Inventory"
        return
    }
    inventoryState.selectedSlot = slot
    selectedSlotLabel.text = getSlotName(slot)
    pageNavigator.isVisible = true
    pageNavigator.pageIndex = 0
    val availableItems = GdxPreferences.account.availableEquipment[slot]
    if (availableItems == null) {
        selectEquipmentSlot(inventoryState, null)
        return
    }
    val itemsCount = availableItems.size
    pageNavigator.pageCount = (itemsCount - 1) / itemsCount + 1
    val loadPage: (Int) -> Unit = {
        val startIndex = it * itemBtnsCount
        for (i in 0 until itemBtnsCount) {
            with(itemBtns[i]) {
                isVisible = if (startIndex + i < itemsCount) {
                    onPressed = { openItemMessage(inventoryState, slot, availableItems[startIndex + i]) }
                    true
                } else false
            }
        }
        updateSelectSlotBtns(inventoryState)
        updateItemBtns(inventoryState)
    }
    pageNavigator.onPageSwitch = loadPage
    loadPage(0)
}

private fun InventoryWidgets.openItemMessage(inventoryState: InventoryState, itemType: EquipmentSlot, itemName: String) {
    val itemData = GdxAssets.getEquipmentData(itemType, itemName)
    itemMessage.setOkAction {
        with(inventoryState.equipment) {
            when (itemType) {
                EquipmentSlot.HELMET -> inventoryState.equipment = copy(helmet = itemName)
                EquipmentSlot.CHEST -> inventoryState.equipment = copy(chest = itemName)
                EquipmentSlot.LEGGINGS -> inventoryState.equipment = copy(legs = itemName)
                EquipmentSlot.CLOAK -> inventoryState.equipment = copy(cloak = itemName)
                EquipmentSlot.MAIN_HAND -> {
                    val itemWeaponType = itemData.weaponType ?: WeaponType.ONE_HANDED
                    val isTwoHanded = itemWeaponType.isTwoHanded()
                    inventoryState.equipment = when {
                        itemName == "null" ->
                            copy(mainHand = "null", offHand = "null", weaponType = WeaponType.UNARMED)
                        isTwoHanded || inventoryState.equipment.weaponType == WeaponType.UNARMED ->
                            copy(mainHand = itemName, offHand = "null", weaponType = itemWeaponType)
                        weaponType.isTwoHanded() ->
                            copy(mainHand = itemName, offHand = itemName, weaponType = WeaponType.DUAL)
                        else ->
                            copy(mainHand = itemName)

                    }
                }
                EquipmentSlot.OFF_HAND -> {
                    val itemWeaponType = itemData.weaponType ?: WeaponType.ONE_HANDED
                    val isTwoHanded = itemWeaponType in arrayOf(WeaponType.TWO_HANDED, WeaponType.BOW, WeaponType.STAFF)
                    inventoryState.equipment = when {
                        isTwoHanded -> {
                            Log.e("Error: Two handed weapons must not be visible in off hand page")
                            this
                        }
                        itemName == "null" ->
                            copy(
                                    offHand = "null",
                                    weaponType = when (weaponType) {
                                        WeaponType.DUAL, WeaponType.SHIELD -> WeaponType.ONE_HANDED
                                        else -> weaponType
                                    }
                            )
                        weaponType.isTwoHanded() || weaponType == WeaponType.UNARMED ->
                            copy(mainHand = itemName, offHand = itemName, weaponType = WeaponType.DUAL)
                        itemWeaponType == WeaponType.SHIELD ->
                            if (weaponType.isTwoHanded() || weaponType == WeaponType.UNARMED)
                                copy(mainHand = "default", offHand = itemName, weaponType = WeaponType.SHIELD)
                            else
                                copy(offHand = itemName, weaponType = WeaponType.SHIELD)
                        else ->
                            copy(offHand = itemName, weaponType = WeaponType.DUAL)
                    }

                }
            }
        }
        itemMessage.isVisible = false
        humanoidWidget.equipment = inventoryState.equipment
        fullDataLabel.text = PlayerAttributes(inventoryState.equipment).toInventoryInfoString()
        updateItemBtns(inventoryState)
        updateSelectSlotBtns(inventoryState)
    }
    itemMessage.title = itemData.name
    itemMessage.description = itemData.toInventoryInfo()
    itemMessage.isVisible = true

}