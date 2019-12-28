package com.mirage.ui.fragments.quests

import com.mirage.core.COMPLETED_QUEST_PHASE
import com.mirage.core.extensions.QuestProgress
import com.mirage.core.preferences.Prefs
import com.mirage.ui.widgets.Button
import com.mirage.ui.widgets.PageNavigator
import kotlin.math.max

internal fun QuestWidgets.initializeListeners(questState: QuestState) {
    updateQuestWindow(questState)
}



internal fun QuestWidgets.updateQuestWindow(questState: QuestState) {
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
                        boundedLabel?.text = QuestLoader.getGuiQuestName(questName, questPhase, questState.gameMapName, questType)
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
                                questState.selectedGlobalQuest = null
                                questState.selectedLocalQuest =
                                        if (questState.selectedLocalQuest == questName) null else questName
                                updateSelectedQuest(questState)
                            }
                        }
                        else {
                            {
                                questState.selectedLocalQuest = null
                                questState.selectedGlobalQuest =
                                        if (questState.selectedGlobalQuest == questName) null else questName
                                updateSelectedQuest(questState)
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
            globalQuestsPageNavigator,
            globalQuestBtns,
            QuestLoader.QuestType.GLOBAL
    )
    updateQuestColumn(
            questState.localQuestProgress,
            localQuestsPageNavigator,
            localQuestBtns,
            QuestLoader.QuestType.LOCAL
    )
    updateSelectedQuest(questState)
}


private fun QuestWidgets.updateSelectedQuest(questState: QuestState) {
    val isQuestSelected: Boolean
    val selectedQuestGuiName: String
    val selectedQuestGuiDescription: String
    val localQ = questState.selectedLocalQuest
    val globalQ = questState.selectedGlobalQuest
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
                selectedQuestGuiName = QuestLoader.getGuiQuestName(
                        questName, questPhase, questState.gameMapName, QuestLoader.QuestType.GLOBAL
                )
                selectedQuestGuiDescription = QuestLoader.getGuiQuestDescription(
                        questName, questPhase, questState.gameMapName, QuestLoader.QuestType.GLOBAL
                )
            }
        }
        else -> {
            val questPhase = questState.localQuestProgress[localQ]
            if (questPhase == null) {
                isQuestSelected = false
                selectedQuestGuiName = ""
                selectedQuestGuiDescription = ""
            }
            else {
                isQuestSelected = true
                selectedQuestGuiName = QuestLoader.getGuiQuestName(
                        localQ, questPhase, questState.gameMapName, QuestLoader.QuestType.LOCAL
                )
                selectedQuestGuiDescription = QuestLoader.getGuiQuestDescription(
                        localQ, questPhase, questState.gameMapName, QuestLoader.QuestType.LOCAL
                )
            }
        }
    }
    questNameLabel.text = selectedQuestGuiName
    questNameLabel.isVisible = isQuestSelected
    questDescriptionLabel.text = selectedQuestGuiDescription
    questDescriptionLabel.isVisible = isQuestSelected
}