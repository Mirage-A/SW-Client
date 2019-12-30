package com.mirage.client

import com.badlogic.gdx.Gdx
import com.google.gson.Gson
import com.mirage.core.preferences.Account
import com.mirage.core.preferences.Preferences
import com.mirage.core.preferences.Profile
import com.mirage.core.preferences.Settings
import com.mirage.core.utils.Log
import com.mirage.core.utils.fromJson

internal object GdxPreferences : Preferences {

    private val gson = Gson()

    override val settings: Settings

    override val account: Account

    private var currentProfile: Profile

    override val profile: Profile
        get() = currentProfile

    init {
        settings = try {
            val settingsPrefs = Gdx.app.getPreferences("SW-Settings")
            gson.fromJson(settingsPrefs.getString("settings"))!!
        } catch (ex: Exception) {
            Log.e("Settings info not found")
            Settings()
        }

        account = try {
            val accountPrefs = Gdx.app.getPreferences("SW-Account")
            gson.fromJson(accountPrefs.getString("account"))!!
        } catch (ex: Exception) {
            Log.e("Account info not found")
            Account()
        }

        val profileName = account.currentProfileName
        currentProfile = if (profileName == null) Profile() else loadProfile(profileName)
    }

    override fun savePreferences() {
        try {
            with(Gdx.app.getPreferences("SW-Settings")) {
                putString("settings", gson.toJson(settings))
                flush()
            }
            with(Gdx.app.getPreferences("SW-Account")) {
                putString("account", gson.toJson(account))
                flush()
            }
            saveCurrentProfile()
        } catch (ex: Exception) {
            Log.e("Error while saving preferences")
        }
    }

    override fun switchProfile(newProfileName: String) {
        saveCurrentProfile()
        currentProfile = loadProfile(newProfileName)
        account.currentProfileName = newProfileName
    }

    private fun loadProfile(profileName: String): Profile = try {
        val profilePrefs = Gdx.app.getPreferences("SW-Profile-$profileName")
        gson.fromJson(profilePrefs.getString("profile"))!!
    } catch (ex: Exception) {
        Log.e("Profile $profileName info not found")
        Profile()
    }

    override fun saveCurrentProfile() {
        try {
            val profileName = account.currentProfileName ?: return
            with(Gdx.app.getPreferences("SW-Profile-$profileName")) {
                putString("profile", gson.toJson(profile))
                flush()
            }
        } catch (ex: Exception) {
            Log.e("Error while saving local profile info")
        }
    }
}