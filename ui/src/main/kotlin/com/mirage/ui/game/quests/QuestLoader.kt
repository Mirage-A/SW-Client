package com.mirage.ui.game.quests

import com.mirage.utils.Assets
import com.mirage.utils.COMPLETED_QUEST_PHASE
import com.mirage.utils.extensions.GameMapName
import com.mirage.utils.extensions.runScript
import com.mirage.utils.extensions.tableOf
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

object QuestLoader {

    /** Returns name of a quest which can be displayed in GUI */
    fun getGuiQuestName(questName: String, questPhase: Int, mapName: GameMapName, questType: QuestType): String {
        val scriptPath =
                if (questType == QuestType.GLOBAL) "quests/global/$questName/name.lua"
                else "quests/local/$mapName/$questName/name.lua"
        return evalScript(scriptPath, questName, questPhase)
    }

    /** Returns description of a quest which can be displayed in GUI */
    fun getGuiQuestDescription(questName: String, questPhase: Int, mapName: GameMapName, questType: QuestType): String {
        val scriptPath =
                if (questType == QuestType.GLOBAL) "quests/global/$questName/description.lua"
                else "quests/local/$mapName/$questName/description.lua"
        return evalScript(scriptPath, questName, questPhase)
    }

    private fun evalScript(scriptPath: String, questName: String, questPhase: Int): String {
        val reader = Assets.loadReader(scriptPath) ?: return "$questName $questPhase"
        return try {
            val answer = AtomicReference("$questName $questPhase")
            val args = tableOf("name" to questName, "phase" to questPhase, "answer" to answer)
            runScript(reader, args)
            answer.get()
        }
        catch (ex: Exception) {
            "$questName $questPhase"
        }
    }

    enum class QuestType {
        LOCAL,
        GLOBAL
    }
}