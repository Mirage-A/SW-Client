package com.mirage.ui.fragments.quests

import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.ui.widgets.Button
import com.mirage.ui.widgets.CompositeWidget
import com.mirage.ui.widgets.ImageWidget
import com.mirage.ui.widgets.LabelWidget
import com.mirage.ui.widgets.PageNavigator

private const val questHeadLabelFontSize = 32f
private const val questPageLabelFontSize = 24f

private const val questNameFontSize = 24f
private const val questDescriptionFontSize = 18f

private const val questBtnFontSize = 16f

internal class QuestWidgets(virtualScreen: VirtualScreen) {

    val backgroundImage = ImageWidget(textureName = "ui/game/quests/quest-window-background")

    val globalQuestsLabel = LabelWidget(virtualScreen, "Global quests", questHeadLabelFontSize)

    val globalQuestsLeftArrow = Button(
            textureName = "ui/game/quests/left-arrow",
            highlightedTextureName = "ui/game/quests/left-arrow-highlighted"
    )

    val globalQuestsRightArrow = Button(
            textureName = "ui/game/quests/right-arrow",
            highlightedTextureName = "ui/game/quests/right-arrow-highlighted"
    )

    val globalQuestsPageLabel = LabelWidget(virtualScreen, "", questPageLabelFontSize)

    val globalQuestsPageNavigator = PageNavigator(
            initialPageIndex = 0,
            initialPageCount = 1,
            leftButton = globalQuestsLeftArrow,
            rightButton = globalQuestsRightArrow,
            pageTextLabel = globalQuestsPageLabel
    )

    val globalQuestBtns = Array(questBtnCount) {
        Button(
                textureName = "ui/game/quests/quest-btn-active",
                highlightedTextureName = "ui/game/quests/quest-btn-active-highlighted",
                boundedLabel = LabelWidget(virtualScreen, "Global quest $it", questBtnFontSize)
        )
    }


    val localQuestsLabel = LabelWidget(virtualScreen, "Local quests", questHeadLabelFontSize)

    val localQuestsLeftArrow = Button(
            textureName = "ui/game/quests/left-arrow",
            highlightedTextureName = "ui/game/quests/left-arrow-highlighted"
    )

    val localQuestsRightArrow = Button(
            textureName = "ui/game/quests/right-arrow",
            highlightedTextureName = "ui/game/quests/right-arrow-highlighted"
    )

    val localQuestsPageLabel = LabelWidget(virtualScreen, "", questPageLabelFontSize)

    val localQuestsPageNavigator = PageNavigator(
            initialPageIndex = 0,
            initialPageCount = 1,
            leftButton = localQuestsLeftArrow,
            rightButton = localQuestsRightArrow,
            pageTextLabel = localQuestsPageLabel
    )

    val localQuestBtns = Array(questBtnCount) {
        Button(
                textureName = "ui/game/quests/quest-btn-active",
                highlightedTextureName = "ui/game/quests/quest-btn-active-highlighted",
                boundedLabel = LabelWidget(virtualScreen, "Local quest $it", questBtnFontSize)
        )
    }

    val questNameLabel = LabelWidget(
            virtualScreen = virtualScreen,
            text = "Quest name",
            fontCapHeight = questNameFontSize,
            isVisible = false)

    val questDescriptionLabel = LabelWidget(
            virtualScreen = virtualScreen,
            text = "Quest description",
            fontCapHeight = questDescriptionFontSize,
            isVisible = false
    )
    private val globalQuestsColumn = CompositeWidget(*globalQuestBtns, globalQuestsPageNavigator, globalQuestsLabel)
    private val localQuestsColumn = CompositeWidget(*localQuestBtns, localQuestsPageNavigator, localQuestsLabel)

    val rootWidget = CompositeWidget(questNameLabel, questDescriptionLabel, globalQuestsColumn, localQuestsColumn, backgroundImage)
}