package com.mirage.ui.screens.loading

import com.mirage.core.game.maps.SceneLoader
import com.mirage.core.preferences.GdxPreferences

internal class LoadingState {
    val gameMapName = GdxPreferences.profile.currentMap
    val gameMap = SceneLoader(gameMapName).loadMap()
}