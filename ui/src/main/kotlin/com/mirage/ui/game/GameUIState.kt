package com.mirage.ui.game

import com.mirage.ui.game.quests.QuestWindow
import com.mirage.ui.widgets.*
import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.extensions.GameMapName
import com.mirage.utils.extensions.QuestProgress
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.game.states.SimplifiedState
import com.mirage.utils.virtualscreen.VirtualScreen

private const val skillPaneMargin = 8f // Отступ между навыками, между навыками и полосой здоровья и между полосой здоровья и экраном
private const val skillIconSize = 64f
private const val skillBorderSize = 4f
private const val skillBtnSize = skillIconSize + skillBorderSize * 2f

private const val skillCoolDownFontSize = 20f
private const val ultimateSkillBtnSize = 134f
private const val ultimateIconSize = 128f
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

internal class GameUIState(val virtualScreen: VirtualScreen, val gameMapName: GameMapName) {

    val localQuestProgress: QuestProgress = QuestProgress()

    var bufferedMoving: Boolean? = null
    var bufferedMoveDirection: MoveDirection? = null
    var lastSentMoving: Boolean? = null
    var lastSentMoveDirection: MoveDirection? = null

    var lastRenderedState: SimplifiedState = SimplifiedState()
    var targetID: Long? = null
    var playerID: Long? = null


    val skillNames: MutableList<String?> = mutableListOf("flame-strike", "flame-strike", "flame-strike", "flame-strike", "meteor")
    val skillCoolDowns: MutableList<Long> = MutableList(5) {0L}
    val activeSkills: List<Button> = List(4) {
        Button(
                textureName = "null",
                boundedLabel = virtualScreen.createLabel("", skillCoolDownFontSize),
                borderSize = skillBorderSize,
                borderTextureName = "ui/game/skill-border"
        ).apply {
            isVisible = false
        }
    }

    val ultimateSkillBtn = CircleButton(
            textureName = "null",
            boundedLabel = virtualScreen.createLabel("", ultimateCoolDownFontSize),
            borderSize = skillBorderSize,
            borderTextureName = "ui/game/ultimate-border"
    ).apply { isVisible = false }

    init {
        activeSkills[0].sizeUpdater = { _, h ->
            Rectangle( - (ultimateSkillBtnSize / 2f + skillPaneMargin * 2f + skillBtnSize * 1.5f),
                    - h / 2f + skillPaneMargin * 2f + playerHealthHeight + skillBtnSize / 2f,
                    skillBtnSize, skillBtnSize)
        }
        activeSkills[1].sizeUpdater = { _, h ->
            Rectangle( - (ultimateSkillBtnSize / 2f + skillPaneMargin + skillBtnSize * 0.5f),
                    - h / 2f + skillPaneMargin * 2f + playerHealthHeight + skillBtnSize / 2f,
                    skillBtnSize, skillBtnSize)
        }
        activeSkills[2].sizeUpdater = { _, h ->
            Rectangle( ultimateSkillBtnSize / 2f + skillPaneMargin + skillBtnSize * 0.5f,
                    - h / 2f + skillPaneMargin * 2f + playerHealthHeight + skillBtnSize / 2f,
                    skillBtnSize, skillBtnSize)
        }
        activeSkills[3].sizeUpdater = { _, h ->
            Rectangle(ultimateSkillBtnSize / 2f + skillPaneMargin * 2f + skillBtnSize * 1.5f,
                    - h / 2f + skillPaneMargin * 2f + playerHealthHeight + skillBtnSize / 2f,
                    skillBtnSize, skillBtnSize)
        }
        ultimateSkillBtn.sizeUpdater = { _, h ->
            Pair(Point(0f, - h / 2f + skillPaneMargin * 2f + playerHealthHeight + ultimateSkillBtnSize / 2f), ultimateSkillBtnSize / 2f)
        }
    }

    /** Updates skill buttons images and cooldown text */
    fun updateSkillBtns() {
        for (i in 0 until 4) {
            val skill = skillNames[i]
            val btn = activeSkills[i]
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
        val skill = skillNames[4]
        val cd = skillCoolDowns[4]
        if (skill == null) {
            ultimateSkillBtn.isVisible = false
        }
        else {
            ultimateSkillBtn.isVisible = true
            ultimateSkillBtn.textureName = if (cd == 0L) "skills/colored/$skill" else "skills/uncolored/$skill"
            ultimateSkillBtn.highlightedTextureName = ultimateSkillBtn.textureName
            ultimateSkillBtn.pressedTextureName = "skills/uncolored/$skill"
            ultimateSkillBtn.boundedLabel?.text = if (cd == 0L) "" else cd.toString()
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

    val questsBtn = Button("ui/game/quests",
            "ui/game/quests-highlighted",
            "ui/game/quests-pressed",
            Rectangle(),
            null,
            {w, h -> Rectangle(w / 2 - microMenuBtnSize * 3 / 2 - microMenuMargin * 2,
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


    val microMenuBtnList: List<Button> = listOf(settingsBtn, questsBtn)
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

    val questWindow = QuestWindow(virtualScreen)

    val widgets: List<Widget> =
            settingsMenuBtnList + confirmExitMessage + microMenuBtnList + questWindow.widget +
                    activeSkills + ultimateSkillBtn +
                    playerHealthPane + targetHealthPane + targetNameArea

    fun resize(virtualWidth: Float, virtualHeight: Float) {
        widgets.forEach { it.resize(virtualWidth, virtualHeight) }
    }

    init {
        resize(virtualScreen.width, virtualScreen.height)
    }

}