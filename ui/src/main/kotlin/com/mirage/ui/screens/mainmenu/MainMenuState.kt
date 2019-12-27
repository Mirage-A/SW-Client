package com.mirage.ui.screens.mainmenu

import com.mirage.core.preferences.Prefs

internal class MainMenuState {
    val newGame: Boolean = Prefs.account.currentProfile == null
}