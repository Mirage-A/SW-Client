package com.mirage.ui.game.quests

import com.mirage.ui.widgets.*
import com.mirage.core.datastructures.Rectangle
import com.mirage.core.virtualscreen.VirtualScreen
import kotlin.math.min

private const val defaultBackgroundWidth = 1000f
private const val defaultBackgroundHeight = 600f
private const val defaultContentWidth = 800f
private const val defaultContentHeight = 500f
private const val questListWidthPart = 0.25f

private const val questHeadLabelHeight = 40f
private const val questHeadLabelFontSize = 32f
private const val questPageLabelFontSize = 24f
private const val questArrowWidth = 68f
private const val questArrowHeight = 18f

private const val questNameFontSize = 24f
private const val questDescriptionFontSize = 18f

private const val defaultQuestBtnHeight = 40f
private const val questBtnFontSize = 16f

internal const val questBtnCount = ((defaultContentHeight - 2 * questHeadLabelHeight) / defaultQuestBtnHeight).toInt()

internal class QuestWindow(val virtualScreen: VirtualScreen) {

    private fun getWindowScale(virtualWidth: Float, virtualHeight: Float) =
            min(virtualWidth / defaultBackgroundWidth, virtualHeight / defaultBackgroundHeight)

    private fun getBackgroundRect(virtualWidth: Float, virtualHeight: Float): Rectangle {
        val scale = getWindowScale(virtualWidth, virtualHeight)
        return Rectangle(0f, 0f, defaultBackgroundWidth * scale, defaultBackgroundHeight * scale)
    }

    private fun getContentRect(virtualWidth: Float, virtualHeight: Float): Rectangle {
        val scale = getWindowScale(virtualWidth, virtualHeight)
        return Rectangle(0f, 0f, defaultContentWidth * scale, defaultContentHeight * scale)
    }

    /** Only 1 of these can be not-null at a time */
    var selectedLocalQuest: String? = null
    var selectedGlobalQuest: String? = null

    val backgroundImage = ImageWidget("ui/game/quests/quest-window-background") {
        w, h -> getBackgroundRect(w, h)
    }

    private fun getGlobalQuestsX(virtualWidth: Float, virtualHeight: Float): Float {
        val contentRect = getContentRect(virtualWidth, virtualHeight)
        return -contentRect.width * (0.5f - questListWidthPart / 2f)
    }

    private fun getLocalQuestsX(virtualWidth: Float, virtualHeight: Float) = -getGlobalQuestsX(virtualWidth, virtualHeight)

    val globalQuestsLabel = LabelWidget(virtualScreen.createLabel("Global quests", questHeadLabelFontSize)) {
        w, h -> Rectangle(getGlobalQuestsX(w, h), getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) / 2f,
            getContentRect(w, h).width * questListWidthPart, questHeadLabelHeight * getWindowScale(w, h))
    }

    val globalQuestsLeftArrow = Button(
            "ui/game/quests/left-arrow",
            "ui/game/quests/left-arrow-highlighted",
            "ui/game/quests/left-arrow-pressed",
            sizeUpdater = {
                w, h -> Rectangle(
                    -getContentRect(w, h).width / 2f + questArrowWidth / 2f + 4f,
                    getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 3f / 2f,
                    questArrowWidth, questArrowHeight
                )
            }
    )

    val globalQuestsRightArrow = Button(
            "ui/game/quests/right-arrow",
            "ui/game/quests/right-arrow-highlighted",
            "ui/game/quests/right-arrow-pressed",
            sizeUpdater = {
                w, h -> Rectangle(
                    getContentRect(w, h).width * (questListWidthPart - 0.5f) - questArrowWidth / 2f - 4f,
                    getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 3f / 2f,
                    questArrowWidth, questArrowHeight
                )
            }
    )

    val globalQuestsPageLabel = LabelWidget(virtualScreen.createLabel("", questPageLabelFontSize)) {
        w, h -> Rectangle(getGlobalQuestsX(w, h), getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 3f / 2f,
            getContentRect(w, h).width * questListWidthPart, questHeadLabelHeight * getWindowScale(w, h))
    }

    val globalQuestsPageNavigator = PageNavigator(0, 1, globalQuestsLeftArrow, globalQuestsRightArrow, globalQuestsPageLabel)

    val globalQuestBtns = Array(questBtnCount) {
        Button(
                "ui/game/quests/quest-btn-active",
                "ui/game/quests/quest-btn-active-highlighted",
                "ui/game/quests/quest-btn-active-pressed",
                boundedLabel = virtualScreen.createLabel("Global quest $it", questBtnFontSize),
                sizeUpdater = {
                    w, h -> Rectangle(
                        getGlobalQuestsX(w, h),
                        getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 2 - defaultQuestBtnHeight * getWindowScale(w, h) * (it + 0.5f),
                        getContentRect(w, h).width * questListWidthPart, defaultQuestBtnHeight * getWindowScale(w, h)
                    )
                }
        )
    }

    val globalQuestsColumn = CompositeWidget(*globalQuestBtns, globalQuestsPageNavigator, globalQuestsLabel)

    val localQuestsLabel = LabelWidget(virtualScreen.createLabel("Local quests", questHeadLabelFontSize)) {
        w, h -> Rectangle(getLocalQuestsX(w, h),
            getContentRect(w, h).height / 2f - questHeadLabelHeight / 2f,
            getContentRect(w, h).width * questListWidthPart, questHeadLabelHeight)
    }
    val localQuestsLeftArrow = Button(
            "ui/game/quests/left-arrow",
            "ui/game/quests/left-arrow-highlighted",
            "ui/game/quests/left-arrow-pressed",
            sizeUpdater = {
                w, h -> Rectangle(
                    - (getContentRect(w, h).width * (questListWidthPart - 0.5f) - questArrowWidth / 2f - 4f),
                    getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 3f / 2f,
                    questArrowWidth, questArrowHeight
            )
            }
    )

    val localQuestsRightArrow = Button(
            "ui/game/quests/right-arrow",
            "ui/game/quests/right-arrow-highlighted",
            "ui/game/quests/right-arrow-pressed",
            sizeUpdater = {
                w, h -> Rectangle(
                    - (-getContentRect(w, h).width / 2f + questArrowWidth / 2f + 4f),
                    getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 3f / 2f,
                    questArrowWidth, questArrowHeight
            )
            }
    )

    val localQuestsPageLabel = LabelWidget(virtualScreen.createLabel("", questPageLabelFontSize)) {
        w, h -> Rectangle(getLocalQuestsX(w, h), getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 3f / 2f,
            getContentRect(w, h).width * questListWidthPart, questHeadLabelHeight * getWindowScale(w, h))
    }

    val localQuestsPageNavigator = PageNavigator(0, 1, localQuestsLeftArrow, localQuestsRightArrow, localQuestsPageLabel)

    val localQuestBtns = Array(questBtnCount) {
        Button(
                "ui/game/quests/quest-btn-active",
                "ui/game/quests/quest-btn-active-highlighted",
                "ui/game/quests/quest-btn-active-pressed",
                boundedLabel = virtualScreen.createLabel("Local quest $it", questBtnFontSize),
                sizeUpdater = {
                    w, h -> Rectangle(
                        getLocalQuestsX(w, h),
                        getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 2 - defaultQuestBtnHeight * getWindowScale(w, h) * (it + 0.5f),
                        getContentRect(w, h).width * questListWidthPart, defaultQuestBtnHeight * getWindowScale(w, h)
                )
                }
        )
    }

    val localQuestsColumn = CompositeWidget(*localQuestBtns, localQuestsPageNavigator, localQuestsLabel)

    val questNameLabel = LabelWidget(virtualScreen.createLabel("Quest name", questNameFontSize)) {
        w, h -> Rectangle(0f, getContentRect(w, h).height / 2f - questHeadLabelHeight,
            getContentRect(w, h).width * (1f - 2 * questListWidthPart), questHeadLabelHeight * 2f)
    }.apply { isVisible = false }

    val questDescriptionLabel = LabelWidget(virtualScreen.createLabel("Quest description", questDescriptionFontSize)) {
        w, h -> Rectangle(0f, -questHeadLabelHeight,
            getContentRect(w, h).width * (1f - 2 * questListWidthPart),
            getContentRect(w, h).height - questHeadLabelHeight * 2f)
    }.apply { isVisible = false }

    val widget = CompositeWidget(questNameLabel, questDescriptionLabel, globalQuestsColumn, localQuestsColumn, backgroundImage).apply { isVisible = false }

}