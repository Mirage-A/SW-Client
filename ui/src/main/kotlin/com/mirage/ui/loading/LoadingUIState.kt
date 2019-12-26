package com.mirage.ui.loading

import com.mirage.core.DEFAULT_SCREEN_HEIGHT
import com.mirage.core.DEFAULT_SCREEN_WIDTH
import com.mirage.ui.inventory.InventoryWindow
import com.mirage.ui.widgets.*
import com.mirage.core.datastructures.Rectangle
import com.mirage.core.extensions.GameMapName
import com.mirage.core.game.maps.SceneLoader
import com.mirage.core.preferences.Prefs
import com.mirage.core.virtualscreen.VirtualScreen
import kotlin.math.max

private const val btnWidth = 400f
private const val btnHeight = 80f
private const val columnWidth = btnWidth + 160f
private const val columnInnerMargin = 32f
private const val columnInnerWidth = columnWidth - columnInnerMargin * 2f
private const val sceneNameLabelHeight = 80f
private const val sceneNameFontSize = 48f
private const val sceneDescriptionFontSize = 32f
private const val defaultArtWidth = DEFAULT_SCREEN_WIDTH - columnWidth
private const val defaultArtHeight = DEFAULT_SCREEN_HEIGHT

class LoadingUIState(val virtualScreen: VirtualScreen, val gameMapName: GameMapName) {

    val inventoryWindow = InventoryWindow(virtualScreen) {}

    private val gameMap = SceneLoader(gameMapName).loadMap()

    fun getArtScale(virtualWidth: Float, virtualHeight: Float): Float =
            max((virtualWidth - columnWidth) / defaultArtWidth, virtualHeight / defaultArtHeight)

    val sceneArt = ImageWidget("../scenes/$gameMapName/art") {
        w, h -> Rectangle(
            - columnWidth / 2f, 0f,
            defaultArtWidth * getArtScale(w, h), defaultArtHeight * getArtScale(w, h)
        )
    }

    val columnBackground = ImageWidget("ui/inventory/inventory-center-background") {
        w, h -> Rectangle(w / 2f - columnWidth / 2f, 0f, columnWidth, h)
    }

    val sceneNameLabel = LabelWidget(virtualScreen.createLabel(gameMap.name ?: gameMapName, sceneNameFontSize)) {
        w, h -> Rectangle(
            w / 2f - columnWidth / 2f, h / 2f - columnInnerMargin - sceneNameLabelHeight / 2f,
            columnInnerWidth, sceneNameLabelHeight
        )
    }

    val sceneDescriptionLabel = LabelWidget(virtualScreen.createLabel(gameMap.description ?: "", sceneDescriptionFontSize)) {
        w, h -> Rectangle(
            w / 2f - columnWidth / 2f, (btnHeight * 4f - sceneNameLabelHeight) / 2f,
            columnInnerWidth, h - columnInnerMargin * 2f - sceneNameLabelHeight - btnHeight * 4f
        )
    }

    val startGameBtn = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Start game", 30f),
            {w, h -> Rectangle(w / 2f - columnWidth / 2f, - h / 2f + btnHeight * 3.5f + columnInnerMargin, btnWidth, btnHeight)})

    val openInventoryBtn = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Open inventory", 30f),
            {w, h -> Rectangle(w / 2f - columnWidth / 2f, - h / 2f + btnHeight * 2.5f + columnInnerMargin, btnWidth, btnHeight)})

    val openSkillsBtn = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Open skills menu", 30f),
            {w, h -> Rectangle(w / 2f - columnWidth / 2f, - h / 2f + btnHeight * 1.5f + columnInnerMargin, btnWidth, btnHeight)})

    val mainMenuBtn = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Main menu", 30f),
            {w, h -> Rectangle(w / 2f - columnWidth / 2f, - h / 2f + btnHeight * 0.5f + columnInnerMargin, btnWidth, btnHeight)})

    val compositeWidget = CompositeWidget(
            inventoryWindow, startGameBtn, openInventoryBtn, openSkillsBtn, mainMenuBtn,
            sceneNameLabel, sceneDescriptionLabel, columnBackground, sceneArt
    )


    fun resize(virtualWidth: Float, virtualHeight: Float) {
        compositeWidget.resize(virtualWidth, virtualHeight)
    }

    init {
        resize(virtualScreen.width, virtualScreen.height)
    }
}