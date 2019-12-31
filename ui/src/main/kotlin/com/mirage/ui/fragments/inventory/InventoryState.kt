package com.mirage.ui.fragments.inventory

import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.objects.properties.EquipmentSlot
import com.mirage.core.preferences.Preferences
import com.mirage.core.utils.Assets
import com.mirage.core.utils.EquipmentLoader

internal class InventoryState(
        val assets: Assets,
        val preferences: Preferences
) {
    val equipmentLoader = EquipmentLoader(assets)
    var initialEquipment: Equipment = preferences.profile.currentEquipment
    var equipment: Equipment = initialEquipment.copy()
    var selectedSlot: EquipmentSlot? = null
}