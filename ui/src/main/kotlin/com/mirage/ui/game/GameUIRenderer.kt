package com.mirage.ui.game

import com.mirage.utils.virtualscreen.VirtualScreen

internal interface GameUIRenderer {

    /**
     * Отрисовывает интерфейс на виртуальном экране с учетом того, что центр камеры находится на координатах (0, 0)
     */
    fun renderUI(virtualScreen: VirtualScreen, uiState: GameUIState, currentTimeMillis: Long)


}