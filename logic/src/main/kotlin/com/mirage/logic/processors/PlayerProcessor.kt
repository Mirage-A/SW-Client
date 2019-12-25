package com.mirage.logic.processors

import com.mirage.logic.LogicData
import com.mirage.core.extensions.*
import com.mirage.core.game.objects.extended.ExtendedEntity
import com.mirage.core.messaging.HumanoidEquipmentUpdateMessage
import com.mirage.core.messaging.InitialGameStateMessage
import org.luaj.vm2.LuaValue


private fun LogicData.createPlayer(): ExtendedEntity = sceneLoader.loadEntityTemplate("player").with(
        x = gameMap.spawnX,
        y = gameMap.spawnY
)

internal fun LogicData.processNewPlayerRequests(currentTime: TimeMillis, deltaTime: IntervalMillis, coercedScriptActions: LuaValue) {
    newPlayerRequests.processNewItems {
        val player = createPlayer()
        val id = state.addEntity(player)
        playerIDs.add(id)
        //TODO Загрузка информации о навыках игрока через сообщения при входе в игру и через хранение профиля в БД
        val skills = listOf("flame-strike", "flame-strike", "flame-strike", "flame-strike", "meteor")
        playerSkills[id] = skills
        playerGlobalQuestProgress[id] = it.questProgress
        playerLocalQuestProgress[id] = QuestProgress()
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