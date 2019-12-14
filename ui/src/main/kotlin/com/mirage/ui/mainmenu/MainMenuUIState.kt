package com.mirage.ui.mainmenu

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.Align
import com.mirage.ui.widgets.*
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.preferences.Prefs
import com.mirage.utils.virtualscreen.VirtualScreen

private const val btnWidth = 400f
private const val btnHeight = 80f
private const val profileWindowWidth = 480f
private const val profileWindowX = - btnWidth / 2f - profileWindowWidth / 2f
private const val profileBodyHeight = 440f
internal const val profileBtnCount = 4
private const val profileBtnMargin = 20f
private const val profileArrowSize = 40f
private const val profileArrowMargin = (btnHeight - profileArrowSize) / 2f
private const val profileArrowShift = profileWindowWidth / 2f - profileArrowMargin - profileArrowSize / 2f


class MainMenuUIState(val virtualScreen: VirtualScreen) {

/*
    private val field = TextField("text", TextField.TextFieldStyle(BitmapFont(), Color.BLACK, null, null, null)
    ).apply {
        isVisible = true
        setSize(100f, 50f)
        setPosition(0f, 0f, Align.center)
        alignment = Align.center
    }

    init {
        virtualScreen.stage.addActor(field)
    }
*/


    val singlePlayerBtn = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Campaign", 30f),
            {_, virtualHeight -> Rectangle(0f, - virtualHeight / 2 + btnHeight * 7 / 2, btnWidth, btnHeight)})

    val multiPlayerBtn = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Multiplayer", 30f),
            {_, virtualHeight -> Rectangle(0f, - virtualHeight / 2 + btnHeight * 5 / 2, btnWidth, btnHeight)})

    val settingsBtn = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Settings", 30f),
            {_, virtualHeight -> Rectangle(0f, - virtualHeight / 2 + btnHeight * 3 / 2, btnWidth, btnHeight)})

    val exitBtn = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Exit", 30f),
            {_, virtualHeight -> Rectangle(0f, - virtualHeight / 2 + btnHeight / 2, btnWidth, btnHeight)})

    val profileNameArea = Button("ui/main-menu-profile-area",
            "ui/main-menu-profile-area",
            "ui/main-menu-profile-area",
            Rectangle(),
            virtualScreen.createLabel(Prefs.account.currentProfile.get() ?: "", 30f),
            {_, virtualHeight -> Rectangle(0f, virtualHeight / 2 - btnHeight / 2, btnWidth, btnHeight)})

    val changeProfileBtn = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Change profile", 30f),
            {_, virtualHeight -> Rectangle(0f, virtualHeight / 2 - btnHeight * 3 / 2, btnWidth, btnHeight)})

    val profileWindowHead = ImageWidget("ui/main-menu/profile-head") {
        _, h ->
        Rectangle(profileWindowX, h / 2f - btnHeight / 2f, profileWindowWidth, btnHeight)
    }

    val profileWindowBody = ImageWidget("ui/main-menu/profile-body") {
        _, h ->
        Rectangle(profileWindowX, h / 2f - btnHeight - profileBodyHeight / 2f, profileWindowWidth, profileBodyHeight)
    }

    val profileWindowButtons = Array(profileBtnCount) {
        Button("ui/main-menu-btn",
                "ui/main-menu-btn-highlighted",
                "ui/main-menu-btn-pressed",
                Rectangle(),
                virtualScreen.createLabel("", 30f),
                {_, h -> Rectangle(profileWindowX, h / 2f - btnHeight * (it + 1.5f) - profileBtnMargin * (it + 1), btnWidth, btnHeight)})
    }

    val profileWindowLeftArrow = Button("ui/main-menu/profile-left-arrow",
            "ui/main-menu/profile-left-arrow",
            "ui/main-menu/profile-left-arrow",
            Rectangle(),
            null,
            {_, h -> Rectangle(profileWindowX - profileArrowShift, h / 2f - btnHeight / 2f, profileArrowSize, profileArrowSize)})


    val profileWindowRightArrow = Button("ui/main-menu/profile-right-arrow",
            "ui/main-menu/profile-right-arrow",
            "ui/main-menu/profile-right-arrow",
            Rectangle(),
            null,
            {_, h -> Rectangle(profileWindowX + profileArrowShift, h / 2f - btnHeight / 2f, profileArrowSize, profileArrowSize)})

    val profileWindowPageLabel = LabelWidget(virtualScreen.createLabel("", 30f)) {
        _, h -> Rectangle(profileWindowX, h / 2f - btnHeight / 2f, profileWindowWidth, btnHeight)
    }

    val profileWindow = CompositeWidget(
            *profileWindowButtons, profileWindowLeftArrow, profileWindowRightArrow, profileWindowPageLabel, profileWindowHead, profileWindowBody
    ).apply { isVisible = false }


    val widgets: List<Widget> = listOf(singlePlayerBtn, multiPlayerBtn, settingsBtn, exitBtn, profileNameArea, changeProfileBtn, profileWindow)

    var currentProfilePage = 0


    fun resize(virtualWidth: Float, virtualHeight: Float) {
        for (btn in widgets) {
            btn.resize(virtualWidth, virtualHeight)
        }
    }

    init {
        resize(virtualScreen.width, virtualScreen.height)
    }
}