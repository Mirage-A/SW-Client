package com.mirage.core.preferences

import kotlin.math.min

/**
 * Creates a new profile with name [name], with starting specialization [selectedClass].
 * [selectedClass] can be equal to "mage", "rogue" or "warrior".
 * @return true if profile was successfully created.
 */
fun createNewProfile(name: String, selectedClass: String): Boolean {
    val filtered = name.filter { it.isLetterOrDigit() }
    if (filtered.isEmpty()) return false
    val validatedName = filtered.substring(0, min(filtered.length, 16))
    if (validatedName in Prefs.account.profiles) return false
    println("Creating profile {$validatedName} and class $selectedClass")
    Prefs.account.profiles.add(validatedName)
    Prefs.switchProfile(validatedName)
    with (Prefs.profile) {
        profileName = validatedName
        when (selectedClass) {
            "mage" -> {
                //TODO Default mage skills, map, equipment etc.
            }
            "rogue" -> {
                //TODO Default rogue skills, map, equipment etc.
            }
            "warrior" -> {
                //TODO Default warrior skills, map, equipment etc.
            }
            else -> return false
        }
    }
    return true
}