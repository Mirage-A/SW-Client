package com.mirage.ui.screens.mainmenu

import com.mirage.core.preferences.Prefs
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.ui.widgets.*


internal class MainMenuWidgets(virtualScreen: VirtualScreen, mainMenuState: MainMenuState) {

    val backgroundArt = ImageWidget(textureName = "ui/art")

    val singlePlayerBtn = Button(
            boundedLabel = LabelWidget(virtualScreen, "Campaign", 30f)
    )

    val multiPlayerBtn = Button(
            boundedLabel = LabelWidget(virtualScreen, "Multiplayer", 30f),
            isVisible = !mainMenuState.newGame
    )

    val settingsBtn = Button(
            boundedLabel = LabelWidget(virtualScreen, "Settings", 30f)
    )

    val exitBtn = Button(
            boundedLabel = LabelWidget(virtualScreen, "Exit", 30f)
    )

    val profileNameArea = Button(
            boundedLabel = LabelWidget(virtualScreen, Prefs.account.currentProfile ?: "", 30f),
            isVisible = !mainMenuState.newGame
    )

    val changeProfileBtn = Button(
            boundedLabel = LabelWidget(virtualScreen, "Change profile", 30f),
            isVisible = !mainMenuState.newGame
    )

    val profileWindowHead = ImageWidget(textureName = "ui/main-menu/profile-head")

    val profileWindowBody = ImageWidget(textureName = "ui/main-menu/profile-body")

    val profileWindowButtons = Array(profileBtnCount) {
        Button(
                boundedLabel = LabelWidget(virtualScreen, "", 30f)
        )
    }

    val profileWindowLeftArrow = Button(textureName = "ui/main-menu/profile-left-arrow")


    val profileWindowRightArrow = Button(textureName = "ui/main-menu/profile-right-arrow")

    val profileWindowPageLabel = LabelWidget(virtualScreen, "", 30f)

    val profilePageNavigator = PageNavigator(
            initialPageIndex = 0,
            initialPageCount = 1,
            leftButton = profileWindowLeftArrow,
            rightButton = profileWindowRightArrow,
            pageTextLabel = profileWindowPageLabel
    )

    val profileWindow = CompositeWidget(
            *profileWindowButtons, profilePageNavigator, profileWindowHead, profileWindowBody,
            isVisible = false
    )

    val rootWidget = CompositeWidget(
            singlePlayerBtn, multiPlayerBtn, settingsBtn, exitBtn, profileNameArea, changeProfileBtn, profileWindow, backgroundArt
    )
}