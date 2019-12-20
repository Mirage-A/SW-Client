package com.mirage.utils.preferences

import java.util.*
import kotlin.collections.ArrayList

class Account {

    val profiles = ArrayList<String>()

    /** To change current profile, use [Prefs.switchProfile] **/
    var currentProfile: String? = null
        internal set

    val availableSkills = arrayListOf<String>()

    val availableEquipment = hashMapOf(
            EquipmentSlot.HELMET to arrayListOf("default"),
            EquipmentSlot.CHEST to arrayListOf("default"),
            EquipmentSlot.LEGGINGS to arrayListOf("default"),
            EquipmentSlot.CLOAK to arrayListOf("default"),
            EquipmentSlot.MAIN_HAND to arrayListOf("default"),
            EquipmentSlot.OFF_HAND to arrayListOf("default") // Just copy one-handed weapons here
    )
}