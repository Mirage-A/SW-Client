package com.mirage.ui.fragments.quests

import com.mirage.core.utils.GdxAssets
import com.mirage.core.utils.GameMapName
import com.mirage.core.utils.runScript
import com.mirage.core.utils.tableOf
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
        val reader = GdxAssets.loadReader(scriptPath) ?: return "$questName $questPhase"
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