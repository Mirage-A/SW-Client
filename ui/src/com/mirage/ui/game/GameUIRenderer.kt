package com.mirage.ui.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch

interface GameUIRenderer {

    /**
     * Отрисовывает интерфейс на виртуальном экране с учетом того, что центр камеры находится на координатах (0, 0)
     */
    fun renderUI(batch: SpriteBatch, screenWidth: Int, screenHeight: Int, uiState: GameUIState, currentTimeMillis: Long)

}