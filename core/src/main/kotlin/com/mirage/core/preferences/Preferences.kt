package com.mirage.core.preferences

interface Preferences {

    val settings: Settings

    val account: Account

    val profile: Profile

    fun savePreferences()

    fun switchProfile(newProfileName: String)

    fun saveCurrentProfile()

}