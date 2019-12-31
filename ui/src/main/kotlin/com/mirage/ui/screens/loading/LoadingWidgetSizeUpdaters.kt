package com.mirage.ui.screens.loading

import com.mirage.core.DEFAULT_SCREEN_HEIGHT
import com.mirage.core.DEFAULT_SCREEN_WIDTH
import com.mirage.core.utils.Rectangle
import kotlin.math.max


private const val btnWidth = 400f
private const val btnHeight = 80f
private const val columnWidth = btnWidth + 160f
private const val columnInnerMargin = 32f
private const val columnInnerWidth = columnWidth - columnInnerMargin * 2f
private const val sceneNameLabelHeight = 80f
private const val defaultArtWidth = DEFAULT_SCREEN_WIDTH - columnWidth
private const val defaultArtHeight = DEFAULT_SCREEN_HEIGHT

internal fun LoadingWidgets.initializeSizeUpdaters() {
    sceneArt.sizeUpdater = { w, h ->
        val scale = max((w - columnWidth) / defaultArtWidth, h / defaultArtHeight)
        Rectangle(
                -columnWidth / 2f, 0f,
                defaultArtWidth * scale, defaultArtHeight * scale
        )
    }
    columnBackground.sizeUpdater = { w, h ->
        Rectangle(w / 2f - columnWidth / 2f, 0f, columnWidth, h)
    }
    sceneNameLabel.sizeUpdater = { w, h ->
        Rectangle(
                w / 2f - columnWidth / 2f, h / 2f - columnInnerMargin - sceneNameLabelHeight / 2f,
                columnInnerWidth, sceneNameLabelHeight
        )
    }
    sceneDescriptionLabel.sizeUpdater = { w, h ->
        Rectangle(
                w / 2f - columnWidth / 2f, (btnHeight * 4f - sceneNameLabelHeight) / 2f,
                columnInnerWidth, h - columnInnerMargin * 2f - sceneNameLabelHeight - btnHeight * 4f
        )
    }
    startGameBtn.sizeUpdater = { w, h ->
        Rectangle(w / 2f - columnWidth / 2f, -h / 2f + btnHeight * 3.5f + columnInnerMargin, btnWidth, btnHeight)
    }
    openInventoryBtn.sizeUpdater = { w, h ->
        Rectangle(w / 2f - columnWidth / 2f, -h / 2f + btnHeight * 2.5f + columnInnerMargin, btnWidth, btnHeight)
    }
    openSkillsBtn.sizeUpdater = { w, h ->
        Rectangle(w / 2f - columnWidth / 2f, -h / 2f + btnHeight * 1.5f + columnInnerMargin, btnWidth, btnHeight)
    }
    mainMenuBtn.sizeUpdater = { w, h ->
        Rectangle(w / 2f - columnWidth / 2f, -h / 2f + btnHeight * 0.5f + columnInnerMargin, btnWidth, btnHeight)
    }
}