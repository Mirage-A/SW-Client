package com.mirage.ui.fragments.inventory

import com.mirage.core.utils.Rectangle
import kotlin.math.min


private const val saveCancelBtnHeight = 80f
private const val saveCancelBtnMargin = 40f
internal const val centerColumnWidthPart = 0.25f
private const val equipmentPartIconSize = 128f
private const val equipmentPartIconBorderSize = 8f
private const val equipmentPartBorderedBtnSize = equipmentPartIconSize + 2f * equipmentPartIconBorderSize
private const val equipmentBtnMargin = 16f
private const val leftBackgroundTextureWidth = 900f
internal const val humanoidScale = 4f
internal const val humanoidSize = humanoidScale * 128f
private const val slotLabelHeight = 64f
private const val pageNavigatorHeight = 64f
private const val arrowSize = 50f
private const val arrowMargin = (pageNavigatorHeight - arrowSize) / 2f

internal fun InventoryWidgets.initializeSizeUpdaters() {
    centerBackground.sizeUpdater = { w, h ->
        Rectangle(0f, 0f, w * centerColumnWidthPart, h)
    }
    saveBtn.sizeUpdater = { w, h ->
        Rectangle(0f, -h / 2f + saveCancelBtnMargin + saveCancelBtnHeight * 1.5f,
                w * centerColumnWidthPart - 2f * saveCancelBtnMargin, saveCancelBtnHeight)
    }
    cancelBtn.sizeUpdater = { w, h ->
        Rectangle(0f, -h / 2f + saveCancelBtnMargin + saveCancelBtnHeight / 2f,
                w * centerColumnWidthPart - 2f * saveCancelBtnMargin, saveCancelBtnHeight)
    }
    fullDataLabel.sizeUpdater = { w, h ->
        Rectangle(0f, saveCancelBtnHeight + saveCancelBtnMargin,
                w * centerColumnWidthPart - 2f * saveCancelBtnMargin,
                h - saveCancelBtnHeight * 2f - saveCancelBtnHeight * 4f
        )
    }
    leftBackground.sizeUpdater = { w, h ->
        Rectangle(getLeftColumnCenterX(w), 0f, leftBackgroundTextureWidth, h)
    }
    for (i in selectSlotBtns.indices) {
        val xIndex = if (i < 4) i else i - 3
        val yIndex = i / 4
        selectSlotBtns[i].sizeUpdater = { w, h ->
            Rectangle(
                    x = getLeftColumnCenterX(w) + (getBorderedBtnSize(w) + equipmentBtnMargin) * (xIndex - 1.5f),
                    y = -h / 2f + getBorderedBtnSize(w) * (yIndex + 0.5f) + equipmentBtnMargin * (yIndex + 1f),
                    width = getBorderedBtnSize(w),
                    height = getBorderedBtnSize(w)
            )
        }
    }
    rightBackground.sizeUpdater = { w, h ->
        Rectangle(getRightColumnCenterX(w), 0f, w * (1f - centerColumnWidthPart) / 2f, h)
    }
    selectedSlotLabel.sizeUpdater = { w, h ->
        Rectangle(
                getRightColumnCenterX(w), h / 2f - slotLabelHeight / 2f,
                w * (1f - centerColumnWidthPart) / 2f, slotLabelHeight
        )
    }
    leftArrow.sizeUpdater = { w, h ->
        Rectangle(
                getRightColumnCenterX(w) - getRightColumnWidth(w) / 2f + arrowMargin + arrowSize / 2f,
                h / 2f - slotLabelHeight - pageNavigatorHeight / 2f,
                arrowSize, arrowSize
        )
    }
    rightArrow.sizeUpdater = { w, h ->
        Rectangle(
                getRightColumnCenterX(w) + getRightColumnWidth(w) / 2f - arrowMargin - arrowSize / 2f,
                h / 2f - slotLabelHeight - pageNavigatorHeight / 2f,
                arrowSize, arrowSize
        )
    }
    pageLabel.sizeUpdater = { w, h ->
        Rectangle(
                getRightColumnCenterX(w), h / 2f - slotLabelHeight - pageNavigatorHeight / 2f,
                getRightColumnWidth(w), pageNavigatorHeight
        )
    }
    for (i in itemBtns.indices) {
        itemBtns[i].sizeUpdater = { w, h ->
            Rectangle(
                    x = getRightColumnCenterX(w) + getItemBtnSize(w, h) * (i % 4 - 1.5f) + equipmentBtnMargin * (i % 4 - 1.5f),
                    y = -(slotLabelHeight + pageNavigatorHeight) / 2f -
                            getItemBtnSize(w, h) * (i / 4 - 1.5f) -
                            equipmentBtnMargin * (i / 4 - 1.5f),
                    width = getItemBtnSize(w, h),
                    height = getItemBtnSize(w, h)
            )
        }
    }
}

private fun getLeftColumnCenterX(virtualWidth: Float): Float =
        -virtualWidth * centerColumnWidthPart / 2f - (virtualWidth / 2f - virtualWidth * centerColumnWidthPart / 2f) / 2f

private fun getRightColumnCenterX(virtualWidth: Float): Float = -getLeftColumnCenterX(virtualWidth)

private fun getBorderedBtnSize(virtualWidth: Float): Float =
        min(equipmentPartBorderedBtnSize, (virtualWidth * (1f - centerColumnWidthPart) / 2f - 5f * equipmentBtnMargin) / 4f)

private fun getRightColumnWidth(virtualWidth: Float): Float =
        virtualWidth * (1f - centerColumnWidthPart) / 2f

private fun getItemBtnSize(virtualWidth: Float, virtualHeight: Float): Float =
        min(getBorderedBtnSize(virtualWidth), (virtualHeight - slotLabelHeight - pageNavigatorHeight / 5 * equipmentBtnMargin) / 4f)