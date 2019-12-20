package com.mirage.utils.preferences

import com.badlogic.gdx.Gdx
import com.google.gson.Gson
import com.mirage.utils.Log
import com.mirage.utils.extensions.fromJson

object Prefs {

    private val gson = Gson()

    val settings: Settings

    val account: Account

    var profile: Profile
        private set

    init {
        settings = try {
            val settingsPrefs = Gdx.app.getPreferences("SW-Settings")
            gson.fromJson(settingsPrefs.getString("settings"))!!
        }
        catch(ex: Exception) {
            Log.e("Settings info not found")
            Settings()
        }

        account = try {
            val accountPrefs = Gdx.app.getPreferences("SW-Account")
            gson.fromJson(accountPrefs.getString("account"))!!
        }
        catch(ex: Exception) {
            Log.e("Account info not found")
            Account()
        }

        val profileName = account.currentProfile
        profile = if (profileName == null) Profile() else loadProfile(profileName)
    }

    fun savePreferences() {
        try {
            with (Gdx.app.getPreferences("SW-Settings")) {
                putString("settings", gson.toJson(settings))
                flush()
            }
            with (Gdx.app.getPreferences("SW-Account")) {
                putString("account", gson.toJson(account))
                flush()
            }
            saveCurrentProfile()
        }
        catch(ex: Exception) {
            Log.e("Error while saving preferences")
        }
    }

    fun switchProfile(newProfileName: String) {
        saveCurrentProfile()
        profile = loadProfile(newProfileName)
        account.currentProfile = newProfileName
    }

    private fun loadProfile(profileName: String): Profile = try {
        val profilePrefs = Gdx.app.getPreferences("SW-Profile-$profileName")
        gson.fromJson(profilePrefs.getString("profile"))!!
    }
    catch(ex: Exception) {
        Log.e("Profile $profileName info not found")
        Profile()
    }

    fun saveCurrentProfile() {
        try {
            val profileName = account.currentProfile ?: return
            with (Gdx.app.getPreferences("SW-Profile-$profileName")) {
                putString("profile", gson.toJson(profile))
                flush()
            }
        }
        catch(ex: Exception) {
            Log.e("Error while saving local profile info")
        }
    }
}