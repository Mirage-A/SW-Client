package com.mirage.ui.fragments.inventory

import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.objects.properties.EquipmentSlot
import com.mirage.core.preferences.Prefs

internal class InventoryState {
    var initialEquipment: Equipment = Prefs.profile.currentEquipment
    var equipment: Equipment = initialEquipment.copy()
    var selectedSlot: EquipmentSlot? = null
}