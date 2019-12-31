package com.mirage.ui.screens.mainmenu

import com.mirage.core.preferences.Preferences
import com.mirage.core.utils.Assets

internal class MainMenuState(
        val assets: Assets,
        val preferences: Preferences
) {
    val newGame: Boolean = preferences.account.currentProfileName == null
}