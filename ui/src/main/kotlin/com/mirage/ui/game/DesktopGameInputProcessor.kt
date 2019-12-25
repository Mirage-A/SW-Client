package com.mirage.ui.game

import com.badlogic.gdx.Input
import com.mirage.ui.game.quests.QuestLoader
import com.mirage.ui.game.quests.questBtnCount
import com.mirage.ui.widgets.Button
import com.mirage.ui.widgets.PageNavigator
import com.mirage.core.COMPLETED_QUEST_PHASE
import com.mirage.core.datastructures.Point
import com.mirage.core.datastructures.rangeBetween
import com.mirage.core.extensions.QuestProgress
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.messaging.*
import com.mirage.core.preferences.Prefs
import rx.subjects.Subject
import kotlin.math.max

internal class DesktopGameInputProcessor(private val uiState: GameUIState) : GameInputProcessor {

    override val inputMessages: Subject<ClientMessage, ClientMessage> = EventSubjectAdapter()

    init {
        uiState.settingsBtn.onPressed = {
            for (btn in uiState.settingsMenuBtnList) {
                btn.isVisible = !btn.isVisible
            }
        }
        uiState.questsBtn.onPressed = {
            uiState.questWindow.widget.isVisible = !uiState.questWindow.widget.isVisible
        }
        uiState.leaveGameBtn.onPressed = {
            uiState.confirmExitMessage.isVisible = true
        }
        uiState.confirmExitMessage.setOkAction {
            inputMessages.onNext(ChangeSceneClientMessage(ChangeSceneClientMessage.Scene.MAIN_MENU))
            uiState.confirmExitMessage.isVisible = false
        }
        uiState.confirmExitMessage.setCancelAction {
            uiState.confirmExitMessage.isVisible = false
        }
        updateQuestWindow()
    }

    private fun updateSelectedQuest() {
        val isQuestSelected: Boolean
        val selectedQuestGuiName: String
        val selectedQuestGuiDescription: String
        val localQ = uiState.questWindow.selectedLocalQuest
        val globalQ = uiState.questWindow.selectedGlobalQuest
        when {
            localQ == null && globalQ == null -> {
                isQuestSelected = false
                selectedQuestGuiName = ""
                selectedQuestGuiDescription = ""
            }
            localQ == null -> {
                val questName = globalQ ?: "null"
                val questPhase = Prefs.profile.globalQuestProgress[questName]
                if (questPhase == null) {
                    isQuestSelected = false
                    selectedQuestGuiName = ""
                    selectedQuestGuiDescription = ""
                }
                else {
                    isQuestSelected = true
                    selectedQuestGuiName = QuestLoader.getGuiQuestName(questName, questPhase, uiState.gameMapName, QuestLoader.QuestType.GLOBAL)
                    selectedQuestGuiDescription = QuestLoader.getGuiQuestDescription(questName, questPhase, uiState.gameMapName, QuestLoader.QuestType.GLOBAL)
                }
            }
            else -> {
                val questPhase = uiState.localQuestProgress[localQ]
                if (questPhase == null) {
                    isQuestSelected = false
                    selectedQuestGuiName = ""
                    selectedQuestGuiDescription = ""
                }
                else {
                    isQuestSelected = true
                    selectedQuestGuiName = QuestLoader.getGuiQuestName(localQ, questPhase, uiState.gameMapName, QuestLoader.QuestType.LOCAL)
                    selectedQuestGuiDescription = QuestLoader.getGuiQuestDescription(localQ, questPhase, uiState.gameMapName, QuestLoader.QuestType.LOCAL)
                }
            }
        }
        uiState.questWindow.questNameLabel.label.text = selectedQuestGuiName
        uiState.questWindow.questNameLabel.isVisible = isQuestSelected
        uiState.questWindow.questDescriptionLabel.label.text = selectedQuestGuiDescription
        uiState.questWindow.questDescriptionLabel.isVisible = isQuestSelected
    }

    override fun updateQuestWindow() {
        fun sortQuests(quests: QuestProgress) =
                // Active quests
                quests.filter { it.value in 1 until COMPLETED_QUEST_PHASE }.toList() +
                        // Not received quests
                        quests.filter { it.value == 0 }.toList() +
                        // Completed quests
                        quests.filter { it.value >= COMPLETED_QUEST_PHASE }.toList() +
                        // Failed quests
                        quests.filter { it.value < 0 }.toList()

        fun updateQuestColumn(progress: QuestProgress, navigator: PageNavigator, btns: Array<Button>, questType: QuestLoader.QuestType) {
            val quests = sortQuests(progress)
            val pages = max(1, (quests.size - 1) / questBtnCount + 1)
            if (navigator.pageIndex >= pages) navigator.pageIndex = pages - 1
            navigator.pageCount = pages
            val pageUpdater: (Int) -> Unit = { page ->
                val startBtnIndex = questBtnCount * page
                for (i in 0 until questBtnCount) {
                    btns[i].apply {
                        if (startBtnIndex + i >= quests.size) {
                            isVisible = false
                        }
                        else {
                            isVisible = true
                            val (questName, questPhase) = quests[startBtnIndex + i]
                            boundedLabel?.text = QuestLoader.getGuiQuestName(questName, questPhase, uiState.gameMapName, questType)
                            val state = when {
                                questPhase >= COMPLETED_QUEST_PHASE -> "completed"
                                questPhase < 0 -> "failed"
                                else -> "active"
                            }
                            textureName = "ui/game/quests/quest-btn-$state"
                            highlightedTextureName = "ui/game/quests/quest-btn-$state-highlighted"
                            pressedTextureName = "ui/game/quests/quest-btn-$state-pressed"
                            onPressed = if (questType == QuestLoader.QuestType.LOCAL) {
                                {
                                    uiState.questWindow.selectedGlobalQuest = null
                                    uiState.questWindow.selectedLocalQuest =
                                            if (uiState.questWindow.selectedLocalQuest == questName) null else questName
                                    updateSelectedQuest()
                                }
                            }
                            else {
                                {
                                    uiState.questWindow.selectedLocalQuest = null
                                    uiState.questWindow.selectedGlobalQuest =
                                            if (uiState.questWindow.selectedGlobalQuest == questName) null else questName
                                    updateSelectedQuest()
                                }
                            }
                        }
                    }
                }
            }
            navigator.onPageSwitch = pageUpdater
            pageUpdater(navigator.pageIndex)
        }
        updateQuestColumn(
                Prefs.profile.globalQuestProgress,
                uiState.questWindow.globalQuestsPageNavigator,
                uiState.questWindow.globalQuestBtns,
                QuestLoader.QuestType.GLOBAL
        )
        updateQuestColumn(
                uiState.localQuestProgress,
                uiState.questWindow.localQuestsPageNavigator,
                uiState.questWindow.localQuestBtns,
                QuestLoader.QuestType.LOCAL
        )
        updateSelectedQuest()
    }

    /**
     * Время отпускания клавиши передвижения
     */
    private var wReleasedTime = 0L
    private var aReleasedTime = 0L
    private var sReleasedTime = 0L
    private var dReleasedTime = 0L

    /**
     * Интервал времени, за который должны быть отпущены две соседние клавиши передвижения,
     * чтобы персонаж остался в диагональном направлении движения
     */
    private val epsTime = 50L

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.widgets.forEach { it.unpress() }
        uiState.widgets.forEach { if (it.touchUp(virtualPoint)) return true}
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.widgets.forEach { if (it.touchDown(virtualPoint)) return true }
        inputMessages.onNext(NewTargetMessage(virtualPoint))
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        val virtualPoint = getVirtualPoint(screenX, screenY)
        uiState.widgets.forEach { it.mouseMoved(virtualPoint)}
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        //println("keyTyped $character")
        return true
    }

    override fun scrolled(amount: Int): Boolean {
        //println("scrolled $amount")
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.W -> {
                wReleasedTime = System.currentTimeMillis()
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.UP_LEFT -> {
                            startMoving(MoveDirection.LEFT)
                        }
                        MoveDirection.UP_RIGHT -> {
                            startMoving(MoveDirection.RIGHT)
                        }
                        MoveDirection.UP -> {
                            if (aReleasedTime >= dReleasedTime && wReleasedTime - aReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.UP_LEFT)
                            } else if (dReleasedTime >= aReleasedTime && wReleasedTime - dReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.UP_RIGHT)
                            }
                            stopMoving()
                        }
                        else -> {
                        }
                    }
                }
            }
            Input.Keys.A -> {
                aReleasedTime = System.currentTimeMillis()
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.UP_LEFT -> {
                            startMoving(MoveDirection.UP)
                        }
                        MoveDirection.DOWN_LEFT -> {
                            startMoving(MoveDirection.DOWN)
                        }
                        MoveDirection.LEFT -> {
                            if (wReleasedTime >= sReleasedTime && aReleasedTime - wReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.UP_LEFT)
                            } else if (sReleasedTime >= wReleasedTime && aReleasedTime - sReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.DOWN_LEFT)
                            }
                            stopMoving()
                        }
                        else -> {
                        }
                    }
                }
            }
            Input.Keys.S -> {
                sReleasedTime = System.currentTimeMillis()
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.DOWN_LEFT -> {
                            startMoving(MoveDirection.LEFT)
                        }
                        MoveDirection.DOWN_RIGHT -> {
                            startMoving(MoveDirection.RIGHT)
                        }
                        MoveDirection.DOWN -> {
                            if (aReleasedTime >= dReleasedTime && sReleasedTime - aReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.DOWN_LEFT)
                            } else if (dReleasedTime >= aReleasedTime && sReleasedTime - dReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.DOWN_RIGHT)
                            }
                            stopMoving()
                        }
                        else -> {
                        }
                    }
                }
            }
            Input.Keys.D -> {
                dReleasedTime = System.currentTimeMillis()
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.UP_RIGHT -> {
                            startMoving(MoveDirection.UP)
                        }
                        MoveDirection.DOWN_RIGHT -> {
                            startMoving(MoveDirection.DOWN)
                        }
                        MoveDirection.RIGHT -> {
                            if (wReleasedTime >= sReleasedTime && dReleasedTime - wReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.UP_RIGHT)
                            } else if (sReleasedTime >= wReleasedTime && dReleasedTime - sReleasedTime < epsTime) {
                                setMoveDirection(MoveDirection.DOWN_RIGHT)
                            }
                            stopMoving()
                        }
                        else -> {
                        }
                    }
                }
            }
        }
        for (i in 0 until 4) {
            if (keycode == Prefs.settings.activeSkillBindings[i]) {
                uiState.activeSkills[i].keyPressed = false
            }
        }
        if (keycode == Prefs.settings.ultimateSkillBinding.get()) {
            uiState.ultimateSkillBtn.keyPressed = false
        }
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        //println("touchDragged $screenX $screenY $pointer")
        return true
    }


    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.W -> {
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.LEFT -> {
                            startMoving(MoveDirection.UP_LEFT)
                        }
                        MoveDirection.RIGHT -> {
                            startMoving(MoveDirection.UP_RIGHT)
                        }
                        else -> {
                            startMoving(MoveDirection.UP)
                        }
                    }
                } else {
                    startMoving(MoveDirection.UP)
                }
            }
            Input.Keys.A -> {
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.UP -> {
                            startMoving(MoveDirection.UP_LEFT)
                        }
                        MoveDirection.DOWN -> {
                            startMoving(MoveDirection.DOWN_LEFT)
                        }
                        else -> {
                            startMoving(MoveDirection.LEFT)
                        }
                    }
                } else {
                    startMoving(MoveDirection.LEFT)
                }
            }
            Input.Keys.S -> {
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.LEFT -> {
                            startMoving(MoveDirection.DOWN_LEFT)
                        }
                        MoveDirection.RIGHT -> {
                            startMoving(MoveDirection.DOWN_RIGHT)
                        }
                        else -> {
                            startMoving(MoveDirection.DOWN)
                        }
                    }
                } else {
                    startMoving(MoveDirection.DOWN)
                }
            }
            Input.Keys.D -> {
                if (uiState.bufferedMoving == true) {
                    when (uiState.bufferedMoveDirection) {
                        MoveDirection.UP -> {
                            startMoving(MoveDirection.UP_RIGHT)
                        }
                        MoveDirection.DOWN -> {
                            startMoving(MoveDirection.DOWN_RIGHT)
                        }
                        else -> {
                            startMoving(MoveDirection.RIGHT)
                        }
                    }
                } else {
                    startMoving(MoveDirection.RIGHT)
                }
            }
            Input.Keys.ESCAPE -> {
                inputMessages.onNext(ClearTargetMessage())
            }
            Input.Keys.E -> {
                val targetID = uiState.targetID
                val player = uiState.lastRenderedState.entities[uiState.playerID]
                val target = uiState.lastRenderedState.entities[targetID]
                if (targetID != null && player != null && target != null &&
                        rangeBetween(player.position, target.position) < target.interactionRange) {
                    inputMessages.onNext(InteractionClientMessage(targetID))
                }
            }
        }
        for (i in 0 until 4) {
            if (keycode == Prefs.settings.activeSkillBindings[i]) {
                inputMessages.onNext(CastSkillClientMessage(i, uiState.targetID))
                uiState.activeSkills[i].keyPressed = true
            }
        }
        if (keycode == Prefs.settings.ultimateSkillBinding.get()) {
            inputMessages.onNext(CastSkillClientMessage(4, uiState.targetID))
            uiState.ultimateSkillBtn.keyPressed = true
        }
        return true
    }


    private fun startMoving(md: MoveDirection) {
        uiState.bufferedMoveDirection = md
        uiState.bufferedMoving = true
    }

    private fun stopMoving() {
        uiState.bufferedMoving = false
    }

    private fun setMoveDirection(md: MoveDirection) {
        uiState.bufferedMoveDirection = md
    }


    private fun getVirtualPoint(screenX: Int, screenY: Int) =
            uiState.virtualScreen.projectRealPointOnVirtualScreen(Point(screenX.toFloat(), screenY.toFloat()))

}