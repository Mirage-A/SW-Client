package com.mirage.ui.mainmenu

import com.mirage.ui.game.GameUIState
import com.mirage.utils.virtualscreen.VirtualScreen

interface MainMenuUIRenderer {

    /**
     * Отрисовывает интерфейс на виртуальном экране с учетом того, что центр камеры находится на координатах (0, 0)
     */
    fun renderUI(virtualScreen: VirtualScreen, uiState: MainMenuUIState, currentTimeMillis: Long)

}