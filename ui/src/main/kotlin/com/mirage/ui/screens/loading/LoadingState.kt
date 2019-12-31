package com.mirage.ui.screens.loading

import com.mirage.core.game.maps.SceneLoader
import com.mirage.core.preferences.Preferences
import com.mirage.core.utils.Assets
import com.mirage.core.utils.EquipmentLoader

internal class LoadingState(
        val assets: Assets,
        val preferences: Preferences
) {
    val gameMapName = preferences.profile.currentMap
    val gameMap = SceneLoader(assets, gameMapName).loadMap()
    val equipmentLoader = EquipmentLoader(assets)
}