package com.mirage.ui.loading

import com.mirage.core.virtualscreen.VirtualScreen

interface LoadingUIRenderer {

    /**
     * Отрисовывает интерфейс на виртуальном экране с учетом того, что центр камеры находится на координатах (0, 0)
     */
    fun renderUI(virtualScreen: VirtualScreen, uiState: LoadingUIState, currentTimeMillis: Long)

}