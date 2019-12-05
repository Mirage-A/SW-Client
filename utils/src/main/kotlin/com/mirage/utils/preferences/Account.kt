package com.mirage.utils.preferences

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicReference

class Account {

    val profiles = CopyOnWriteArrayList<String>()

    /** To change current profile, use [Prefs.switchProfile] **/
    val currentProfile = AtomicReference<String?>(null)

    val availableSkills = CopyOnWriteArraySet<String>()

    val availableHelmets = CopyOnWriteArraySet<String>()
    val availableChests = CopyOnWriteArraySet<String>()
    val availableCloaks = CopyOnWriteArraySet<String>()
    val availableGloves = CopyOnWriteArraySet<String>()
    val availableLegs = CopyOnWriteArraySet<String>()
    val availableOneHanded = CopyOnWriteArraySet<String>()
    val availableTwoHanded = CopyOnWriteArraySet<String>()
    val availableBows = CopyOnWriteArraySet<String>()
    val availableStaffs = CopyOnWriteArraySet<String>()
}