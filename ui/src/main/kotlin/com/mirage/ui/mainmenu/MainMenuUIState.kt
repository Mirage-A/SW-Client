package com.mirage.ui.mainmenu

import com.mirage.ui.widgets.Button
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.virtualscreen.VirtualScreen

class MainMenuUIState(val virtualScreen: VirtualScreen) {

    private val btnWidth = 400f
    private val btnHeight = 80f


    val singlePlayerBtn = Button("ui/mainmenubtn",
            "ui/mainmenubtnpressed",
            "ui/mainmenubtnpressed",
            Rectangle(),
            virtualScreen.createLabel("Campaign"),
            {_, virtualHeight -> Rectangle(0f, - virtualHeight / 2 + btnHeight * 7 / 2, btnWidth, btnHeight)})

    val multiPlayerBtn = Button("ui/mainmenubtn",
            "ui/mainmenubtnpressed",
            "ui/mainmenubtnpressed",
            Rectangle(),
            virtualScreen.createLabel("Multiplayer"),
            {_, virtualHeight -> Rectangle(0f, - virtualHeight / 2 + btnHeight * 5 / 2, btnWidth, btnHeight)})

    val settingsBtn = Button("ui/mainmenubtn",
            "ui/mainmenubtnpressed",
            "ui/mainmenubtnpressed",
            Rectangle(),
            virtualScreen.createLabel("Settings"),
            {_, virtualHeight -> Rectangle(0f, - virtualHeight / 2 + btnHeight * 3 / 2, btnWidth, btnHeight)})

    val exitBtn = Button("ui/mainmenubtn",
            "ui/mainmenubtnpressed",
            "ui/mainmenubtnpressed",
            Rectangle(),
            virtualScreen.createLabel("Exit"),
            {_, virtualHeight -> Rectangle(0f, - virtualHeight / 2 + btnHeight / 2, btnWidth, btnHeight)})

    val btnList: List<Button> = listOf(singlePlayerBtn, multiPlayerBtn, settingsBtn, exitBtn)


    fun resize(virtualWidth: Float, virtualHeight: Float) {
        for (btn in btnList) {
            btn.resize(virtualWidth, virtualHeight)
        }
    }

    init {
        resize(virtualScreen.width, virtualScreen.height)
    }
}