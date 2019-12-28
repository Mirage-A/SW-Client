package com.mirage.ui.fragments.quests

import com.mirage.core.Assets
import com.mirage.core.extensions.GameMapName
import com.mirage.core.extensions.runScript
import com.mirage.core.extensions.tableOf
import java.util.concurrent.atomic.AtomicReference

object QuestLoader {

    /** Returns name of a quest which can be displayed in GUI */
    fun getGuiQuestName(questName: String, questPhase: Int, mapName: GameMapName, questType: QuestType): String {
        val scriptPath =
                if (questType == QuestType.GLOBAL) "global-quests/$questName/name.lua"
                else "scenes/$mapName/quests/$questName/name.lua"
        return evalScript(scriptPath, questName, questPhase)
    }

    /** Returns description of a quest which can be displayed in GUI */
    fun getGuiQuestDescription(questName: String, questPhase: Int, mapName: GameMapName, questType: QuestType): String {
        val scriptPath =
                if (questType == QuestType.GLOBAL) "global-quests/$questName/description.lua"
                else "scenes/$mapName/quests/$questName/description.lua"
        return evalScript(scriptPath, questName, questPhase)
    }

    private fun evalScript(scriptPath: String, questName: String, questPhase: Int): String {
        val reader = Assets.loadReader(scriptPath) ?: return "$questName $questPhase"
        return try {
            val answer = AtomicReference("$questName $questPhase")
            val args = tableOf("name" to questName, "phase" to questPhase, "answer" to answer)
            runScript(reader, args)
            answer.get()
        } catch (ex: Exception) {
            "$questName $questPhase"
        }
    }

    enum class QuestType {
        LOCAL,
        GLOBAL
    }
}