package com.mirage.ui.game.quests

import com.mirage.utils.COMPLETED_QUEST_PHASE

object QuestLoader {

    /** Returns name of a quest which can be displayed in GUI */
    fun getGuiQuestName(questName: String, questPhase: Int, questType: QuestType): String {
        return "$questType Quest $questName $questPhase"
    }

    /** Returns description of a quest which can be displayed in GUI */
    fun getGuiQuestDescription(questName: String, questPhase: Int, questType: QuestType): String {
        return "KVEST DASCRIPSHEN"
    }

    enum class QuestType {
        LOCAL,
        GLOBAL
    }
}