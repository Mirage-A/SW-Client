package com.mirage.utils.preferences

class Account {

    val profiles = ArrayList<String>()

    /** To change current profile, use [Prefs.switchProfile] **/
    var currentProfile: String? = null
        internal set

    val availableSkills: MutableSet<String> = HashSet()

    val availableHelmets: MutableSet<String> = HashSet()
    val availableChests: MutableSet<String> = HashSet()
    val availableCloaks: MutableSet<String> = HashSet()
    val availableGloves: MutableSet<String> = HashSet()
    val availableLegs: MutableSet<String> = HashSet()
    val availableOneHanded: MutableSet<String> = HashSet()
    val availableTwoHanded: MutableSet<String> = HashSet()
    val availableBows: MutableSet<String> = HashSet()
    val availableStaffs: MutableSet<String> = HashSet()
}