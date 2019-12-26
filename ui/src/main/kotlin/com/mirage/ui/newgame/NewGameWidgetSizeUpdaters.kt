package com.mirage.ui.newgame

import com.mirage.core.datastructures.Rectangle
import kotlin.math.max
import kotlin.math.min


private const val classBtnSize = 136f
private const val minDescriptionWidth = 400f
private const val classBtnMargin = 48f
private const val classNameLabelHeight = 80f
private const val confirmBtnHeight = 80f

internal fun NewGameWidgets.initializeSizeUpdaters() {
    classArt.sizeUpdater = { w, h ->
        Rectangle(-w / 2f + getClassArtWidth(w, h) / 2f, 0f, h, h)
    }
    descriptionBackground.sizeUpdater = { w, h ->
        Rectangle(w / 2f - max(minDescriptionWidth, w - h) / 2f, 0f, max(minDescriptionWidth, w - h), h)
    }
    warriorBtn.sizeUpdater = { w, h ->
        Rectangle(getWarriorIconX(w, h),
                -h / 2f + classBtnMargin + classBtnSize / 2f,
                classBtnSize, classBtnSize)
    }
    rogueBtn.sizeUpdater = { w, h ->
        Rectangle(getWarriorIconX(w, h) + classBtnSize + classBtnMargin,
                -h / 2f + classBtnMargin + classBtnSize / 2f,
                classBtnSize, classBtnSize)
    }
    mageBtn.sizeUpdater = { w, h ->
        Rectangle(getWarriorIconX(w, h) + classBtnSize * 2f + classBtnMargin * 2f,
                -h / 2f + classBtnMargin + classBtnSize / 2f,
                classBtnSize, classBtnSize)
    }
    classNameLabel.sizeUpdater = { w, h ->
        Rectangle(getDescriptionX(w, h), h / 2f - classNameLabelHeight / 2f, w - getClassArtWidth(w, h), classNameLabelHeight)
    }
    confirmBtn.sizeUpdater = { w, h ->
        Rectangle(getDescriptionX(w, h), -h / 2f + confirmBtnHeight / 2f, w - getClassArtWidth(w, h), confirmBtnHeight)
    }
    descriptionLabel.sizeUpdater = { w, h ->
        Rectangle(getDescriptionX(w, h), 0f, w - getClassArtWidth(w, h), h)
    }
    nameAreaBackground.sizeUpdater = ::nameAreaRect
    profileNameField.sizeUpdater = { w, h ->
        Rectangle(getDescriptionX(w, h), -h / 2f + confirmBtnHeight * 1.5f, w - getClassArtWidth(w, h), confirmBtnHeight)
    }
}

private fun getClassArtWidth(virtualWidth: Float, virtualHeight: Float): Float =
        min(virtualHeight, virtualWidth - minDescriptionWidth)

private fun getWarriorIconX(virtualWidth: Float, virtualHeight: Float): Float =
        min(
                -virtualWidth / 2f + getClassArtWidth(virtualWidth, virtualHeight) / 2f - classBtnSize - classBtnMargin,
                classBtnMargin + classBtnSize / 2f
        )

private fun getDescriptionX(virtualWidth: Float, virtualHeight: Float): Float =
        getClassArtWidth(virtualWidth, virtualHeight) / 2f

private fun nameAreaRect(virtualWidth: Float, virtualHeight: Float) = Rectangle(
        getDescriptionX(virtualWidth, virtualHeight),
        -virtualHeight / 2f + confirmBtnHeight * 3f / 2f + 8f,
        virtualWidth - getClassArtWidth(virtualWidth, virtualHeight),
        confirmBtnHeight
)
