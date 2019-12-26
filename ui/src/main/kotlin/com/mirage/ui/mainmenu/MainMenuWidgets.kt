package com.mirage.ui.mainmenu

import com.mirage.core.datastructures.Rectangle
import com.mirage.core.preferences.Prefs
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.ui.widgets.Button
import com.mirage.ui.widgets.CompositeWidget
import com.mirage.ui.widgets.ImageWidget
import com.mirage.ui.widgets.LabelWidget
import com.mirage.ui.widgets.PageNavigator
import com.mirage.ui.widgets.Widget


internal class MainMenuWidgets(virtualScreen: VirtualScreen) {

    val backgroundArt = ImageWidget(textureName = "ui/art")

    val singlePlayerBtn = Button(
            textureName = "ui/main-menu-btn",
            highlightedTextureName = "ui/main-menu-btn-highlighted",
            boundedLabel = LabelWidget(virtualScreen, "Campaign", 30f)
    )

    val multiPlayerBtn = Button(
            textureName = "ui/main-menu-btn",
            highlightedTextureName = "ui/main-menu-btn-highlighted",
            boundedLabel = LabelWidget(virtualScreen, "Multiplayer", 30f)
    )

    val settingsBtn = Button(
            textureName = "ui/main-menu-btn",
            highlightedTextureName = "ui/main-menu-btn-highlighted",
            boundedLabel = LabelWidget(virtualScreen, "Settings", 30f)
    )

    val exitBtn = Button(
            textureName = "ui/main-menu-btn",
            highlightedTextureName = "ui/main-menu-btn-highlighted",
            boundedLabel = LabelWidget(virtualScreen, "Exit", 30f)
    )

    val profileNameArea = Button(
            textureName = "ui/main-menu-profile-area",
            highlightedTextureName = "ui/main-menu-profile-area",
            boundedLabel = LabelWidget(virtualScreen, Prefs.account.currentProfile ?: "", 30f)
    )

    val changeProfileBtn = Button(
            textureName = "ui/main-menu-btn",
            highlightedTextureName = "ui/main-menu-btn-highlighted",
            boundedLabel = LabelWidget(virtualScreen, "Change profile", 30f)
    )

    val profileWindowHead = ImageWidget(textureName = "ui/main-menu/profile-head")

    val profileWindowBody = ImageWidget(textureName = "ui/main-menu/profile-body")

    val profileWindowButtons = Array(profileBtnCount) {
        Button(
                textureName = "ui/main-menu-btn",
                highlightedTextureName = "ui/main-menu-btn-highlighted",
                boundedLabel = LabelWidget(virtualScreen, "", 30f)
        )
    }

    val profileWindowLeftArrow = Button(
            textureName = "ui/main-menu/profile-left-arrow",
            highlightedTextureName = "ui/main-menu/profile-left-arrow"
    )


    val profileWindowRightArrow = Button(
            textureName = "ui/main-menu/profile-right-arrow",
            highlightedTextureName = "ui/main-menu/profile-right-arrow"
    )

    val profileWindowPageLabel = LabelWidget(virtualScreen, "", 30f)

    val profilePageNavigator = PageNavigator(
            initialPageIndex = 0,
            initialPageCount = 1,
            leftButton = profileWindowLeftArrow,
            rightButton = profileWindowRightArrow,
            pageTextLabel = profileWindowPageLabel
    )

    val profileWindow = CompositeWidget(
            *profileWindowButtons, profilePageNavigator, profileWindowHead, profileWindowBody
    )

    val rootWidget = CompositeWidget(
            singlePlayerBtn, multiPlayerBtn, settingsBtn, exitBtn, profileNameArea, changeProfileBtn, profileWindow, backgroundArt
    )
}