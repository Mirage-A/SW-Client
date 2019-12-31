package com.mirage.ui.screens.game

import com.mirage.core.VirtualScreen
import com.mirage.ui.fragments.gameview.GameViewFragment
import com.mirage.ui.fragments.quests.QuestFragment
import com.mirage.ui.screens.ClientMessageListener
import com.mirage.ui.widgets.*


private const val skillCoolDownFontSize = 20f
private const val ultimateCoolDownFontSize = 40f
private const val playerHealthFontHeight = 24f
private const val targetNameFontHeight = 16f
private const val settingsMenuBtnFontSize = 20f
internal const val screenFadingInterval = 2000L
private const val maxFadingAlpha = 196f / 256f

internal class GameWidgets(virtualScreen: VirtualScreen, gameState: GameState, listener: ClientMessageListener) {

    val gameView = GameViewFragment(gameState, listener)

    val questWindow = QuestFragment(virtualScreen, gameState.gameMapName, gameState.localQuestProgress, gameState.preferences, gameState.assets)

    val activeSkills: Array<Button> = Array(4) {
        Button(
                textureName = "null",
                boundedLabel = LabelWidget(virtualScreen, "", skillCoolDownFontSize),
                borderSize = skillBorderSize,
                borderTextureName = "ui/game/skill-border",
                isVisible = false,
                keyCode = gameState.settings.activeSkillKeys[it]
        )
    }

    val ultimateSkillBtn = CircleButton(
            textureName = "null",
            boundedLabel = LabelWidget(virtualScreen, "", ultimateCoolDownFontSize),
            borderSize = skillBorderSize,
            borderTextureName = "ui/game/ultimate-border",
            isVisible = false,
            keyCode = gameState.settings.ultimateSkillKey
    )

    val settingsBtn = Button(
            textureName = "ui/game/settings",
            highlightedTextureName = "ui/game/settings-highlighted",
            pressedTextureName = "ui/game/settings-pressed"
    )

    val questsBtn = Button(
            textureName = "ui/game/quests",
            highlightedTextureName = "ui/game/quests-highlighted",
            pressedTextureName = "ui/game/quests-pressed"
    )

    val leaveGameBtn = Button(
            boundedLabel = LabelWidget(virtualScreen, "Main menu", settingsMenuBtnFontSize)
    )

    val microMenu = CompositeWidget(settingsBtn, questsBtn)
    val settingsMenu = CompositeWidget(leaveGameBtn, isVisible = false)

    val confirmExitMessage: ConfirmMessage = ConfirmMessage(
            virtualScreen = virtualScreen,
            title = "Main menu",
            description = "Do you want to leave game and open main menu?\nUnsaved progress will be lost.",
            okTitle = "Exit",
            cancelTitle = "Cancel"
    )

    val playerHealthPane: ResourcePane = ResourcePane(
            borderTextureName = "ui/game/health-border",
            lostResourceTextureName = "ui/game/health-lost",
            resourceTextureName = "ui/game/health",
            boundedLabel = LabelWidget(virtualScreen, "", playerHealthFontHeight),
            innerMargin = playerHealthBorderMargin
    )

    val targetHealthPane: ResourcePane = ResourcePane(
            borderTextureName = "ui/game/health-border",
            lostResourceTextureName = "ui/game/health-lost",
            resourceTextureName = "ui/game/health",
            boundedLabel = LabelWidget(virtualScreen, "", playerHealthFontHeight),
            innerMargin = playerHealthBorderMargin
    )

    val targetNameArea: TargetNameArea = TargetNameArea(
            borderTextureName = "ui/game/health-border",
            textAreaTextureName = "ui/game/health-lost",
            boundedLabel = LabelWidget(virtualScreen, "Your target", targetNameFontHeight),
            innerMargin = playerHealthBorderMargin
    )

    val gameOverBackground = ShadowingWidget(
            maxAlpha = maxFadingAlpha,
            shadowingTime = screenFadingInterval,
            isVisible = false,
            blocksInput = true
    )

    val gameOverText = ImageWidget(textureName = "ui/game/game-over")

    val gameOverMessage = Button(
            textureName = "ui/game/game-over-background",
            borderTextureName = "ui/game/game-over-border",
            boundedLabel = LabelWidget(virtualScreen, "You died", 24f),
            borderSize = 4f
    )

    val retryBtn = Button(boundedLabel = LabelWidget(virtualScreen, "Retry", 30f))

    val mainMenuBtn = Button(boundedLabel = LabelWidget(virtualScreen, "Main menu", 30f))

    val gameOverCompositeWidget = CompositeWidget(
            gameOverMessage, retryBtn, mainMenuBtn, gameOverText,
            isVisible = false
    )

    val gameCompositeWidget = CompositeWidget(
            settingsMenu, confirmExitMessage, microMenu, questWindow,
            *activeSkills, ultimateSkillBtn, playerHealthPane, targetHealthPane, targetNameArea, gameView
    )

    val rootWidget = CompositeWidget(
            gameOverCompositeWidget, gameOverBackground, gameCompositeWidget
    )
}