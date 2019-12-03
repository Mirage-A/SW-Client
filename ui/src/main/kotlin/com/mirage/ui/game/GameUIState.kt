package com.mirage.ui.game

import com.mirage.ui.widgets.*
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.game.objects.simplified.SimplifiedEntity
import com.mirage.utils.virtualscreen.VirtualScreen

private const val skillPaneMargin = 8f // Отступ между навыками, между навыками и полосой здоровья и между полосой здоровья и экраном
private const val skillBtnSize = 72f

private const val skillCoolDownFontSize = 20f
private const val ultimateSkillBtnSize = 168f
private const val ultimateCoolDownFontSize = 40f

private const val playerHealthWidth = ultimateSkillBtnSize + skillBtnSize * 4f + skillPaneMargin * 4f
private const val playerHealthHeight = 64f
private const val playerHealthBorderMargin = 4f
private const val playerHealthFontHeight = 24f

private const val targetNameAreaWidth = playerHealthWidth * 0.75f
private const val targetNameAreaHeight = 48f
private const val targetNameFontHeight = 16f

private const val microMenuBtnSize = 64f
private const val microMenuMargin = 8f
private const val settingsMenuBtnWidth = microMenuBtnSize * 3f + microMenuMargin * 2f
private const val settingsMenuBtnFontSize = 20f

/**
 * Состояние интерфейса.
 * Изменяется классами [DesktopGameInputProcessor] (при вводе пользователя) и [GameScreen] (при получении данных от логики).
 * Этот объект и его состояние должно быть защищено блокировкой this
 */
class GameUIState(val virtualScreen: VirtualScreen) {

    var bufferedMoving: Boolean? = null
    var bufferedMoveDirection: MoveDirection? = null
    var lastSentMoving: Boolean? = null
    var lastSentMoveDirection: MoveDirection? = null

    var targetEntity: SimplifiedEntity? = null
    var player: SimplifiedEntity? = null


    val skillNames: MutableList<String?> = mutableListOf("flame-strike", "flame-strike", "flame-strike", "flame-strike", "meteor")
    val skillCoolDowns: MutableList<Long> = MutableList(5) {0L}
    val skillBtns: List<Button> = List(5) {
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


    val settingsBtn = Button("ui/game/settings",
            "ui/game/settings-highlighted",
            "ui/game/settings-pressed",
            Rectangle(),
            null,
            {w, h -> Rectangle(w / 2 - microMenuBtnSize / 2 - microMenuMargin,
                    - h / 2 + microMenuBtnSize / 2 + microMenuMargin,
                    microMenuBtnSize, microMenuBtnSize) })

    val leaveGameBtn = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
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

    val playerHealthPane: ResourcePane = ResourcePane(
            "ui/game/health-border",
            "ui/game/health-lost",
            "ui/game/health",
            Rectangle(),
            virtualScreen.createLabel("", playerHealthFontHeight),
            playerHealthBorderMargin
    ) {
        _, h ->
        Rectangle(0f, - h / 2f + skillPaneMargin + playerHealthHeight / 2f, playerHealthWidth, playerHealthHeight)
    }

    val targetHealthPane: ResourcePane = ResourcePane(
            "ui/game/health-border",
            "ui/game/health-lost",
            "ui/game/health",
            Rectangle(),
            virtualScreen.createLabel("", playerHealthFontHeight),
            playerHealthBorderMargin
    ) {
        _, h ->
        Rectangle(0f, h / 2f - skillPaneMargin - playerHealthHeight / 2f, playerHealthWidth, playerHealthHeight)
    }

    val targetNameLabel = virtualScreen.createLabel("Your target", targetNameFontHeight)
    val targetNameArea: TargetNameArea = TargetNameArea(
            "ui/game/health-border",
            "ui/game/health-lost",
            Rectangle(),
            targetNameLabel,
            playerHealthBorderMargin
    ) {
        _, h ->
        Rectangle(0f, h / 2f - skillPaneMargin - playerHealthHeight - targetNameAreaHeight / 2f + playerHealthBorderMargin,
                targetNameAreaWidth, targetNameAreaHeight)
    }

    val widgets: Collection<Widget> =
            microMenuBtnList + settingsMenuBtnList + skillBtns + confirmExitMessage + playerHealthPane + targetHealthPane + targetNameArea


    fun resize(virtualWidth: Float, virtualHeight: Float) {
        widgets.forEach { it.resize(virtualWidth, virtualHeight) }
    }

    init {
        resize(virtualScreen.width, virtualScreen.height)
    }

}