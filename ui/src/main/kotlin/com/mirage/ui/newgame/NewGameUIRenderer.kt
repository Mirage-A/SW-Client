package com.mirage.ui.newgame

import com.mirage.utils.virtualscreen.VirtualScreen

interface NewGameUIRenderer {

    /**
     * Отрисовывает интерфейс на виртуальном экране с учетом того, что центр камеры находится на координатах (0, 0)
     */
    fun renderUI(virtualScreen: VirtualScreen, uiState: NewGameUIState, currentTimeMillis: Long)

}