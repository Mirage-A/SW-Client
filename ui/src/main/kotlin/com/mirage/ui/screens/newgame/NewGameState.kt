package com.mirage.ui.screens.newgame

import com.mirage.core.preferences.Preferences
import com.mirage.core.utils.Assets

internal class NewGameState(
        val assets: Assets,
        val preferences: Preferences
) {
    var selectedClass = "none"
}