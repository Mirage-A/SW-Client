package com.mirage.ui.screens.loading

import com.mirage.core.game.maps.SceneLoader
import com.mirage.core.preferences.Prefs

internal class LoadingState {
    val gameMapName = Prefs.profile.currentMap
    val gameMap = SceneLoader(gameMapName).loadMap()
}