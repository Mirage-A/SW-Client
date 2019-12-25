package com.mirage.gamelogic.logic

import com.mirage.gamelogic.LogicData
import com.mirage.gamelogic.LogicScriptActions
import com.mirage.utils.extensions.*
import com.mirage.utils.game.objects.extended.ExtendedEntity
import com.mirage.utils.messaging.InitialGameStateMessage
import org.luaj.vm2.LuaValue


private fun LogicData.createPlayer(): ExtendedEntity = sceneLoader.loadEntityTemplate("player").with(
        x = gameMap.spawnX,
        y = gameMap.spawnY
)

internal fun LogicData.handleNewPlayerRequests(currentTime: TimeMillis, deltaTime: IntervalMillis, coercedScriptActions: LuaValue) {
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
        runAssetScript("scenes/$gameMapName/new-player", tableOf("playerID" to id), coercedScriptActions)
        it.onCreate(id)
    }
}

internal fun LogicData.handleRemovePlayerRequests(coercedScriptActions: LuaValue) {
    removePlayerRequests.processNewItems {
        runAssetScript("scenes/$gameMapName/player-exit", tableOf("playerID" to it), coercedScriptActions)
        state.removeEntity(it)
    }
}