package com.mirage.ui.screens.loading

import com.mirage.core.VirtualScreen
import com.mirage.ui.fragments.inventory.InventoryFragment
import com.mirage.ui.widgets.Button
import com.mirage.ui.widgets.CompositeWidget
import com.mirage.ui.widgets.ImageWidget
import com.mirage.ui.widgets.LabelWidget


private const val sceneNameFontSize = 48f
private const val sceneDescriptionFontSize = 32f

internal class LoadingWidgets(virtualScreen: VirtualScreen, loadingState: LoadingState) {

    val inventoryWindow = InventoryFragment(virtualScreen, loadingState.assets, loadingState.preferences)

    val sceneArt = ImageWidget(textureName = "../scenes/${loadingState.gameMapName}/art")

    val columnBackground = ImageWidget(textureName = "ui/inventory/inventory-center-background")

    val sceneNameLabel = LabelWidget(virtualScreen, loadingState.gameMap.name
            ?: loadingState.gameMapName, sceneNameFontSize)

    val sceneDescriptionLabel = LabelWidget(virtualScreen, loadingState.gameMap.description
            ?: "", sceneDescriptionFontSize)

    val startGameBtn = Button(boundedLabel = LabelWidget(virtualScreen, "Start game", 30f))

    val openInventoryBtn = Button(boundedLabel = LabelWidget(virtualScreen, "Open inventory", 30f))

    val openSkillsBtn = Button(
            boundedLabel = LabelWidget(virtualScreen, "Open skills menu", 30f),
            //TODO Remove
            isVisible = false
    )

    val mainMenuBtn = Button(boundedLabel = LabelWidget(virtualScreen, "Main menu", 30f))

    val rootWidget = CompositeWidget(
            inventoryWindow, startGameBtn, openInventoryBtn, openSkillsBtn, mainMenuBtn,
            sceneNameLabel, sceneDescriptionLabel, columnBackground, sceneArt
    )
}