package com.mirage.ui.fragments.quests

import com.mirage.core.utils.Rectangle
import kotlin.math.min


private const val defaultBackgroundWidth = 1000f
private const val defaultBackgroundHeight = 600f
private const val defaultContentWidth = 800f
private const val defaultContentHeight = 500f
private const val questListWidthPart = 0.25f

private const val questHeadLabelHeight = 40f
private const val questArrowWidth = 68f
private const val questArrowHeight = 18f

private const val defaultQuestBtnHeight = 40f

internal const val questBtnCount = ((defaultContentHeight - 2 * questHeadLabelHeight) / defaultQuestBtnHeight).toInt()

internal fun QuestWidgets.initializeSizeUpdaters() {
    backgroundImage.sizeUpdater = { w, h ->
        getBackgroundRect(w, h)
    }
    globalQuestsLabel.sizeUpdater = { w, h ->
        Rectangle(getGlobalQuestsX(w, h), getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) / 2f,
                getContentRect(w, h).width * questListWidthPart, questHeadLabelHeight * getWindowScale(w, h))
    }
    globalQuestsLeftArrow.sizeUpdater = { w, h ->
        Rectangle(
                -getContentRect(w, h).width / 2f + questArrowWidth / 2f + 4f,
                getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 3f / 2f,
                questArrowWidth, questArrowHeight
        )
    }
    globalQuestsRightArrow.sizeUpdater = { w, h ->
        Rectangle(
                getContentRect(w, h).width * (questListWidthPart - 0.5f) - questArrowWidth / 2f - 4f,
                getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 3f / 2f,
                questArrowWidth, questArrowHeight
        )
    }
    globalQuestsPageLabel.sizeUpdater = { w, h ->
        Rectangle(getGlobalQuestsX(w, h), getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 3f / 2f,
                getContentRect(w, h).width * questListWidthPart, questHeadLabelHeight * getWindowScale(w, h))
    }
    for ((i, btn) in globalQuestBtns.withIndex()) {
        btn.sizeUpdater = { w, h ->
            Rectangle(
                    getGlobalQuestsX(w, h),
                    getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 2 - defaultQuestBtnHeight * getWindowScale(w, h) * (i + 0.5f),
                    getContentRect(w, h).width * questListWidthPart,
                    defaultQuestBtnHeight * getWindowScale(w, h)
            )
        }
    }
    localQuestsLabel.sizeUpdater = { w, h ->
        Rectangle(getLocalQuestsX(w, h),
                getContentRect(w, h).height / 2f - questHeadLabelHeight / 2f,
                getContentRect(w, h).width * questListWidthPart, questHeadLabelHeight)
    }
    localQuestsLeftArrow.sizeUpdater = { w, h ->
        Rectangle(
                -(getContentRect(w, h).width * (questListWidthPart - 0.5f) - questArrowWidth / 2f - 4f),
                getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 3f / 2f,
                questArrowWidth, questArrowHeight
        )
    }
    localQuestsRightArrow.sizeUpdater = { w, h ->
        Rectangle(
                -(-getContentRect(w, h).width / 2f + questArrowWidth / 2f + 4f),
                getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 3f / 2f,
                questArrowWidth, questArrowHeight
        )
    }
    localQuestsPageLabel.sizeUpdater = { w, h ->
        Rectangle(getLocalQuestsX(w, h), getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 3f / 2f,
                getContentRect(w, h).width * questListWidthPart, questHeadLabelHeight * getWindowScale(w, h))
    }
    for ((i, btn) in localQuestBtns.withIndex()) {
        btn.sizeUpdater = { w, h ->
            Rectangle(
                    getLocalQuestsX(w, h),
                    getContentRect(w, h).height / 2f - questHeadLabelHeight * getWindowScale(w, h) * 2 - defaultQuestBtnHeight * getWindowScale(w, h) * (i + 0.5f),
                    getContentRect(w, h).width * questListWidthPart,
                    defaultQuestBtnHeight * getWindowScale(w, h)
            )
        }
    }
    questNameLabel.sizeUpdater = { w, h ->
        Rectangle(0f, getContentRect(w, h).height / 2f - questHeadLabelHeight,
                getContentRect(w, h).width * (1f - 2 * questListWidthPart), questHeadLabelHeight * 2f)
    }
    questDescriptionLabel.sizeUpdater = { w, h ->
        Rectangle(0f, -questHeadLabelHeight,
                getContentRect(w, h).width * (1f - 2 * questListWidthPart),
                getContentRect(w, h).height - questHeadLabelHeight * 2f)
    }
}


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

private fun getGlobalQuestsX(virtualWidth: Float, virtualHeight: Float): Float {
    val contentRect = getContentRect(virtualWidth, virtualHeight)
    return -contentRect.width * (0.5f - questListWidthPart / 2f)
}

private fun getLocalQuestsX(virtualWidth: Float, virtualHeight: Float) = -getGlobalQuestsX(virtualWidth, virtualHeight)
