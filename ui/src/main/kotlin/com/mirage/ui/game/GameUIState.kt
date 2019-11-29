package com.mirage.ui.game

import com.mirage.ui.widgets.Button
import com.mirage.ui.widgets.ConfirmMessage
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.virtualscreen.VirtualScreen

/**
 * Состояние интерфейса.
 * Изменяется классами [DesktopGameInputProcessor] (при вводе пользователя) и [GameScreen] (при получении данных от логики).
 * Этот объект и его состояние должно быть защищено блокировкой this
 */
class GameUIState(val virtualScreen: VirtualScreen) {

    var bufferedMoving : Boolean? = null
    var bufferedMoveDirection : GameObject.MoveDirection? = null
    var lastSentMoving : Boolean? = null
    var lastSentMoveDirection : GameObject.MoveDirection? = null

    private val microMenuBtnSize = 64f
    private val microMenuMargin = 16f
    private val settingsMenuBtnWidth = microMenuBtnSize * 3f + microMenuMargin * 2f
    private val settingsMenuBtnFontSize = 20f

    var settingsMenuVisible = false

    val settingsBtn = Button("ui/game/settings",
            "ui/game/settings",
            "ui/game/settings",
            Rectangle(),
            null,
            {w, h -> Rectangle(w / 2 - microMenuBtnSize / 2 - microMenuMargin,
                    - h / 2 + microMenuBtnSize / 2 + microMenuMargin,
                    microMenuBtnSize, microMenuBtnSize) })

    val leaveGameBtn = Button("ui/mainmenubtn",
            "ui/mainmenubtnhighlighted",
            "ui/mainmenubtnpressed",
            Rectangle(),
            virtualScreen.createLabel("Main menu", settingsMenuBtnFontSize),
            {w, h -> Rectangle(w / 2 - settingsMenuBtnWidth / 2f - microMenuMargin,
                    - h / 2 + microMenuBtnSize * 1.5f + microMenuMargin,
                    settingsMenuBtnWidth, microMenuBtnSize) })


    val microMenuBtnList: List<Button> = listOf(settingsBtn)
    val settingsMenuBtnList: List<Button> = listOf(leaveGameBtn)

    val confirmExitMessage : ConfirmMessage = ConfirmMessage(
            virtualScreen,
            "Main menu",
            "Do you want to leave game and open main menu?\nUnsaved progress will be lost.",
            "Exit",
            "Cancel")

    fun resize(virtualWidth: Float, virtualHeight: Float) {
        for (btn in microMenuBtnList) {
            btn.resize(virtualWidth, virtualHeight)
        }
        for (btn in settingsMenuBtnList) {
            btn.resize(virtualWidth, virtualHeight)
        }
    }

    init {
        resize(virtualScreen.width, virtualScreen.height)
    }

}