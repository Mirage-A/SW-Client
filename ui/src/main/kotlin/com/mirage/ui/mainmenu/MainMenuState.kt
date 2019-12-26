package com.mirage.ui.mainmenu

import com.mirage.core.messaging.ClientMessage
import com.mirage.core.preferences.Prefs
import java.util.*

internal class MainMenuState {
    val newGame: Boolean = Prefs.account.currentProfile == null
}