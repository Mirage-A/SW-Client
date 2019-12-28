package com.mirage.logic.processors

import com.mirage.core.extensions.*
import com.mirage.core.game.objects.properties.PlayerAttributes
import com.mirage.core.messaging.HumanoidEquipmentUpdateMessage
import com.mirage.core.messaging.InitialGameStateMessage
import com.mirage.core.utils.*
import com.mirage.logic.behavior.PlayerBehavior
import com.mirage.logic.data.LogicData
import org.luaj.vm2.LuaValue


internal fun LogicData.processNewPlayerRequests(currentTime: TimeMillis, deltaTime: IntervalMillis, coercedScriptActions: LuaValue) {
    newPlayerRequests.processNewItems {
        val attributes = PlayerAttributes(it.equipment)
        val player = sceneLoader.loadEntityTemplate("player").apply {
            name = it.playerName
            x = gameMap.spawnX
            y = gameMap.spawnY
            health = attributes.maxHealth
            maxHealth = attributes.maxHealth
            action = "idle"
            state = "default"
        }
        val id = state.addEntity(player)
        playerIDs.add(id)
        playerGlobalQuestProgress[id] = it.questProgress
        playerLocalQuestProgress[id] = QuestProgress()
        val skills = listOf("flame-strike", "flame-strike", "flame-strike", "flame-strike", "meteor")
        behaviors[id] = PlayerBehavior(id, it.equipment, skills, this)
        serverMessages.add(Pair(id, InitialGameStateMessage(
                gameMapName, state.simplifiedDeepCopy(), id, currentTime - deltaTime
        )))
        serverMessages.add(Pair(-1L, HumanoidEquipmentUpdateMessage(id, it.equipment)))
        runAssetScript("scenes/$gameMapName/new-player", tableOf("playerID" to id), coercedScriptActions)
        it.onCreate(id)
    }
}

internal fun LogicData.processRemovePlayerRequests(coercedScriptActions: LuaValue) {
    removePlayerRequests.processNewItems {
        runAssetScript("scenes/$gameMapName/player-exit", tableOf("playerID" to it), coercedScriptActions)
        state.removeEntity(it)
    }
}