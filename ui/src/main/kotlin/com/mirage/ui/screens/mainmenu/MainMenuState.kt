package com.mirage.ui.screens.mainmenu

import com.mirage.core.preferences.GdxPreferences

internal class MainMenuState {
    val newGame: Boolean = GdxPreferences.account.currentProfileName == null
}