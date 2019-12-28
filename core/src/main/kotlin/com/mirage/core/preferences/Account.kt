package com.mirage.core.preferences

import com.mirage.core.game.objects.properties.EquipmentSlot

class Account {

    val profiles = ArrayList<String>()

    /** To change current profile, use [Prefs.switchProfile] **/
    var currentProfile: String? = null
        internal set

    val availableSkills = arrayListOf<String>()

    val availableEquipment = hashMapOf(
            EquipmentSlot.HELMET to arrayListOf("null", "default"),
            EquipmentSlot.CHEST to arrayListOf("null", "default"),
            EquipmentSlot.LEGGINGS to arrayListOf("null", "default"),
            EquipmentSlot.CLOAK to arrayListOf("null", "default"),
            EquipmentSlot.MAIN_HAND to arrayListOf("null", "default"),
            EquipmentSlot.OFF_HAND to arrayListOf("null", "default") // Just copy one-handed weapons here
    )
}