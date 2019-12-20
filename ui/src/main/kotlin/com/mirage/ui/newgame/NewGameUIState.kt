package com.mirage.ui.newgame

import com.mirage.ui.widgets.Button
import com.mirage.ui.widgets.ImageWidget
import com.mirage.ui.widgets.LabelWidget
import com.mirage.ui.widgets.Widget
import com.mirage.utils.PLATFORM
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.virtualscreen.VirtualScreen
import kotlin.math.max
import kotlin.math.min

private const val classBtnSize = 136f
private const val classBtnBorderSize = 4f
private const val minDescriptionWidth = 400f
private const val classBtnMargin = 48f
private const val classNameLabelHeight = 80f
private const val classNameLabelFontSize = 32f
private const val confirmBtnHeight = 80f
private const val confirmBtnWidth = 400f


class NewGameUIState(val virtualScreen: VirtualScreen) {

    private fun getClassArtWidth(virtualWidth: Float, virtualHeight: Float): Float =
            min(virtualHeight, virtualWidth - minDescriptionWidth)

    private fun getWarriorIconX(virtualWidth: Float, virtualHeight: Float): Float =
            min(
                    - virtualWidth / 2f + getClassArtWidth(virtualWidth, virtualHeight) / 2f - classBtnSize - classBtnMargin,
                    classBtnMargin + classBtnSize / 2f
            )

    private fun getDescriptionX(virtualWidth: Float, virtualHeight: Float): Float =
            getClassArtWidth(virtualWidth, virtualHeight) / 2f

    var selectedClass = "none"

    val classArt = ImageWidget("") {
        w, h -> Rectangle(- w / 2f + getClassArtWidth(w, h) / 2f, 0f, h, h)
    }.apply { isVisible = false }

    val descriptionBackground = ImageWidget("ui/new-game/description-background") {
        w, h -> Rectangle(w / 2f - max(minDescriptionWidth, w - h) / 2f, 0f, max(minDescriptionWidth, w - h), h)
    }

    val warriorBtn = Button(
            "ui/new-game/warrior-icon",
            "ui/new-game/warrior-icon-highlighted",
            "ui/new-game/warrior-icon-highlighted",
            sizeUpdater = { w, h ->
                Rectangle(getWarriorIconX(w, h),
                        - h / 2f + classBtnMargin + classBtnSize / 2f,
                        classBtnSize, classBtnSize)
            },
            borderTextureName = "ui/new-game/description-background",
            borderSize = classBtnBorderSize
    )

    val rogueBtn = Button(
            "ui/new-game/rogue-icon",
            "ui/new-game/rogue-icon-highlighted",
            "ui/new-game/rogue-icon-highlighted",
            sizeUpdater = { w, h ->
                Rectangle(getWarriorIconX(w, h) + classBtnSize + classBtnMargin,
                        - h / 2f + classBtnMargin + classBtnSize / 2f,
                        classBtnSize, classBtnSize)
            },
            borderTextureName = "ui/new-game/description-background",
            borderSize = classBtnBorderSize
    )

    val mageBtn = Button(
            "ui/new-game/mage-icon",
            "ui/new-game/mage-icon-highlighted",
            "ui/new-game/mage-icon-highlighted",
            sizeUpdater = { w, h ->
                Rectangle(getWarriorIconX(w, h) + classBtnSize * 2f + classBtnMargin * 2f,
                        - h / 2f + classBtnMargin + classBtnSize / 2f,
                        classBtnSize, classBtnSize)
            },
            borderTextureName = "ui/new-game/description-background",
            borderSize = classBtnBorderSize
    )

    val classNameLabel = LabelWidget(virtualScreen.createLabel("ASSASSINATION", classNameLabelFontSize)) {
        w, h ->
        Rectangle(getDescriptionX(w, h), h / 2f - classNameLabelHeight / 2f, w - getClassArtWidth(w, h), classNameLabelHeight)
    }.apply { isVisible = false }

    val confirmBtn = Button(
            "ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Confirm", 30f),
            {w, h -> Rectangle(getDescriptionX(w, h), - h / 2f + confirmBtnHeight / 2f, w - getClassArtWidth(w, h), confirmBtnHeight)}
    ).apply { isVisible = false }

    val descriptionLabel = LabelWidget(virtualScreen.createLabel("Choose starting specialization", 24f)) {
        w, h -> Rectangle(getDescriptionX(w, h), 0f, w - getClassArtWidth(w, h), h)
    }

    private fun nameAreaRect(virtualWidth: Float, virtualHeight: Float) = Rectangle(
            getDescriptionX(virtualWidth, virtualHeight),
            - virtualHeight / 2f + confirmBtnHeight * 3f / 2f + 8f,
            virtualWidth - getClassArtWidth(virtualWidth, virtualHeight),
            confirmBtnHeight
    )

    val nameAreaBackground = ImageWidget("ui/new-game/text-field-background", ::nameAreaRect).apply {
        isVisible = PLATFORM == "desktop" || PLATFORM == "desktop-test"
    }

    val textField: VirtualScreen.TextField? =
            if (PLATFORM == "desktop" || PLATFORM == "desktop-test")
                virtualScreen.createTextField("Enter your name", Rectangle(), 20f)
            else
                null

    val widgets: List<Widget> = listOf(
            nameAreaBackground, descriptionLabel, confirmBtn, classNameLabel, warriorBtn, rogueBtn, mageBtn, descriptionBackground, classArt
    )

    fun resize(virtualWidth: Float, virtualHeight: Float) {
        widgets.forEach { it.resize(virtualWidth, virtualHeight) }
        textField?.rect = nameAreaRect(virtualWidth, virtualHeight)
    }

    init {
        resize(virtualScreen.width, virtualScreen.height)
    }
}