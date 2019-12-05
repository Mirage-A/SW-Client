package com.mirage.utils.preferences

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Preferences
import com.google.gson.Gson
import com.mirage.utils.Log
import com.mirage.utils.PLATFORM
import java.lang.Exception

object Prefs {

    private val gson = Gson()

    @Volatile
    var settings: Settings = Settings()


    fun loadProfileInfo() {
        try {
            val settingsPrefs = Gdx.app.getPreferences("SW-settings")
            try {
                settings = gson.fromJson<Settings>(settingsPrefs.getString("settings"), Settings::class.java)
                println("Desktop fullscreen mode: ${settings.desktopFullScreen}")
            }
            catch(ex: Exception) {
                Log.e("Settings info not found")
            }
        }
        catch(ex: Exception) {
            if (PLATFORM != "test" && PLATFORM != "desktop-test") Log.e("Error while loading local profile info")
        }
    }

    fun saveProfileInfo() {
        try {
            val settingsPrefs = Gdx.app.getPreferences("SW-settings")
            settingsPrefs.putString("settings", gson.toJson(settings))
            settingsPrefs.flush()
        }
        catch(ex: Exception) {
            if (PLATFORM != "test" && PLATFORM != "desktop-test") Log.e("Error while saving local profile info")
        }
    }
    /*

    private class StringProperty(
            private val map: MutableMap<String, Any?>,
            private val defaultValue: String
    ) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
                map[property.name] as? String ?: defaultValue

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            map[property.name] = value
        }
    }

    private class BooleanProperty(
            private val map: MutableMap<String, Any?>,
            private val defaultValue: Boolean
    ) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean =
                map[property.name] as? Boolean ?: defaultValue

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
            map[property.name] = value
        }
    }

    private class IntProperty(
            private val map: MutableMap<String, Any?>,
            private val defaultValue: Int
    ) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): Int =
                map[property.name] as? Int ?: defaultValue

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            map[property.name] = value
        }
    }

    private class IntArrayProperty(
            private val map: MutableMap<String, Any?>,
            private val defaultValue: IntArray
    ) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): IntArray =
                IntArray(defaultValue.size) {
                    map[property.name + it] as? Int ?: defaultValue[it]
                }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: IntArray) {
            for (i in value.indices) {
                map[property.name + i] = value[i]
            }
        }
    }

    private class StringArrayProperty(
            private val map: MutableMap<String, Any?>,
            private val defaultValue: Array<String>
    ) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): Array<String> =
                Array(defaultValue.size) {
                    map[property.name + it] as? String ?: defaultValue[it]
                }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Array<String>) {
            for (i in value.indices) {
                map[property.name + i] = value[i]
            }
        }
    }*/

}