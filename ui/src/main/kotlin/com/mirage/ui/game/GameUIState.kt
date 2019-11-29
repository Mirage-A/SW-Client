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

    private val playerHealthWidth = 0f
    private val playerHealthHeight = 64f
    private val skillPaneMargin = 8f // Отступ между навыками, между навыками и полосой здоровья и между полосой здоровья и экраном

    private val skillBtnSize = 72f
    private val skillCoolDownFontSize = 20f
    private val ultimateSkillBtnSize = 168f
    private val ultimateCoolDownFontSize = 40f

    val skillNames : MutableList<String?> = mutableListOf("flamestrike", "flamestrike", "flamestrike", "flamestrike", "meteor")
    val skillCoolDowns: MutableList<Long> = MutableList(5) {0L}
    val skillBtns : List<Button> = List(5) {
        Button(
                textureName = "null",
                boundedLabel = virtualScreen.createLabel("", if (it == 4) ultimateCoolDownFontSize else skillCoolDownFontSize)
        ).apply {
            isVisible = false
        }
    }


    init {
        skillBtns[0].sizeUpdater = { _, h ->
            Rectangle( - (ultimateSkillBtnSize / 2f + skillPaneMargin * 2f + skillBtnSize * 1.5f),
                    - h / 2f + skillPaneMargin * 2f + playerHealthHeight + skillBtnSize / 2f,
                    skillBtnSize, skillBtnSize)
        }
        skillBtns[1].sizeUpdater = { _, h ->
            Rectangle( - (ultimateSkillBtnSize / 2f + skillPaneMargin + skillBtnSize * 0.5f),
                    - h / 2f + skillPaneMargin * 2f + playerHealthHeight + skillBtnSize / 2f,
                    skillBtnSize, skillBtnSize)
        }
        skillBtns[2].sizeUpdater = { _, h ->
            Rectangle( ultimateSkillBtnSize / 2f + skillPaneMargin + skillBtnSize * 0.5f,
                    - h / 2f + skillPaneMargin * 2f + playerHealthHeight + skillBtnSize / 2f,
                    skillBtnSize, skillBtnSize)
        }
        skillBtns[3].sizeUpdater = { _, h ->
            Rectangle(ultimateSkillBtnSize / 2f + skillPaneMargin * 2f + skillBtnSize * 1.5f,
                    - h / 2f + skillPaneMargin * 2f + playerHealthHeight + skillBtnSize / 2f,
                    skillBtnSize, skillBtnSize)
        }
        skillBtns[4].sizeUpdater = { _, h ->
            Rectangle(0f,
                    - h / 2f + skillPaneMargin * 2f + playerHealthHeight + ultimateSkillBtnSize / 2f,
                    ultimateSkillBtnSize, ultimateSkillBtnSize)
        }
    }

    /**
     * Обновляет изображения навыков и текст времени перезарядки.
     * Должен вызываться при любом изменении [skillNames] и [skillCoolDowns]
     */
    fun updateSkillBtns() {
        for (i in 0 until 5) {
            val skill = skillNames[i]
            val btn = skillBtns[i]
            val cd = skillCoolDowns[i]
            if (skill == null) {
                btn.isVisible = false
            }
            else {
                btn.isVisible = true
                btn.textureName = if (cd == 0L) "skills/colored/$skill" else "skills/uncolored/$skill"
                btn.highlightedTextureName = btn.textureName
                btn.pressedTextureName = "skills/uncolored/$skill"
                btn.boundedLabel?.text = if (cd == 0L) "" else cd.toString()
            }
        }
    }

    init {
        updateSkillBtns()
    }

    private val microMenuBtnSize = 64f
    private val microMenuMargin = 8f
    private val settingsMenuBtnWidth = microMenuBtnSize * 3f + microMenuMargin * 2f
    private val settingsMenuBtnFontSize = 20f

    val settingsBtn = Button("ui/game/settings",
            "ui/game/settingshighlighted",
            "ui/game/settingspressed",
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
                    settingsMenuBtnWidth, microMenuBtnSize) }).apply { isVisible = false }


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
        for (btn in skillBtns) {
            btn.resize(virtualWidth, virtualHeight)
        }
    }

    init {
        resize(virtualScreen.width, virtualScreen.height)
    }

}