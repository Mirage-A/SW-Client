package com.mirage.logic

import com.mirage.core.Log
import com.mirage.core.extensions.*
import com.mirage.core.game.maps.GameMap
import com.mirage.core.game.objects.extended.ExtendedBuilding
import com.mirage.core.game.objects.extended.ExtendedEntity
import com.mirage.core.game.states.ExtendedState
import com.mirage.core.messaging.*
import com.mirage.logic.processors.runAssetScript
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.CoerceJavaToLua

internal class LogicScriptActionsImpl(private val data: LogicData) : LogicScriptActions {

    override val coerced: LuaValue = CoerceJavaToLua.coerce(this)

    override fun runSceneScript(scriptName: String, args: LuaTable) {
        runAssetScript("scenes/${data.gameMapName}/scripts/$scriptName", args, coerced)
    }

    override fun runSceneScriptAfterTimeout(scriptName: String, args: LuaTable, timeout: Long) {
        data.delayedScripts.add((System.currentTimeMillis() + timeout) to (scriptName to args))
    }

    override fun getState(): ExtendedState = data.state

    override fun getGameMap(): GameMap = data.gameMap

    override fun getGameMapName(): String = data.gameMapName

    override fun findEntityID(name: String): EntityID? = data.state.entities.entries.find { it.value.name == name }?.key

    override fun findBuildingID(name: String): BuildingID? = data.state.buildings.entries.find { it.value.name == name }?.key

    override fun findEntity(name: String): ExtendedEntity? = data.state.entities.entries.find { it.value.name == name }?.value

    override fun findBuilding(name: String): ExtendedBuilding? = data.state.buildings.entries.find { it.value.name == name }?.value

    override fun getEntity(id: EntityID): ExtendedEntity? = data.state.entities[id]

    override fun findAllEntityIDs(name: String): List<EntityID> =
            data.state.entities.filter { it.value.name == name }.map { it.key }

    override fun findAllBuildingIDs(name: String): List<BuildingID> =
            data.state.buildings.filter { it.value.name == name }.map { it.key }

    override fun findAllPlayerIDs(): List<EntityID> = data.playerIDs.toList()

    override fun addEntity(entity: ExtendedEntity): EntityID {
        val id = data.state.addEntity(entity)
        data.behaviors[id] = data.sceneLoader.loadBehavior(entity.template, id, data)
        return id
    }

    override fun dealDamageToEntity(sourceID: EntityID, entityID: EntityID, damage: Int) {
        data.state.entities[entityID]?.let {
            it.health -= damage
            if (it.health <= 0) {
                it.health = 0
                runAssetScript(
                        "scenes/${data.gameMapName}/templates/entities/${it.template}/death",
                        tableOf("sourceID" to sourceID, "entityID" to entityID),
                        coerced
                )
            }
        }
    }

    override fun destroyEntity(entityID: EntityID) {
        val entity = data.state.entities[entityID] ?: return
        runAssetScript(
                "scenes/${data.gameMapName}/templates/entities/${entity.template}/destroy",
                tableOf("entityID" to entityID),
                coerced
        )
        data.state.removeEntity(entityID)
        data.behaviors.remove(entityID)
    }

    override fun print(msg: Any?) = Log.i(msg)

    override fun setSkillCooldown(playerID: EntityID, skillID: Int, cooldown: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSkillCooldown(playerID: EntityID, skillID: Int): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startDialog(playerID: EntityID, dialogName: String) {
        data.serverMessages.add(Pair(playerID, StartDialogMessage(dialogName)))
    }

    override fun sendTextMessage(playerID: EntityID, message: String) {
        data.serverMessages.add(Pair(playerID, DisplayTextMessage(message)))
    }

    override fun sendTextMessageToAll(message: String) {
        data.serverMessages.add(Pair(-1L, DisplayTextMessage(message)))
    }

    override fun getGlobalQuestPhase(playerID: EntityID, questName: String): Int =
            data.playerGlobalQuestProgress[playerID]?.getOrDefault(questName, 0) ?: 0

    override fun getLocalQuestPhase(playerID: EntityID, questName: String): Int =
            data.playerLocalQuestProgress[playerID]?.getOrDefault(questName, 0) ?: 0

    override fun setGlobalQuestPhase(playerID: EntityID, questName: String, newPhase: Int) {
        data.playerGlobalQuestProgress[playerID]?.let {
            it[questName] = newPhase
            data.serverMessages.add(Pair(playerID, GlobalQuestUpdateMessage(questName, newPhase)))
            val args = tableOf("playerID" to playerID, "phase" to newPhase)
            runAssetScript("global-quests/$questName/update", args, coerced)
        }
    }

    override fun setLocalQuestPhase(playerID: EntityID, questName: String, newPhase: Int) {
        data.playerLocalQuestProgress[playerID]?.let {
            it[questName] = newPhase
            data.serverMessages.add(Pair(playerID, LocalQuestUpdateMessage(questName, newPhase)))
            val args = tableOf("playerID" to playerID, "phase" to newPhase)
            runAssetScript("scenes/${data.gameMapName}/quests/$questName/update", args, coerced)
        }
    }

    override fun sendPlayerToAnotherMap(playerID: EntityID, mapName: GameMapName, code: ReturnCode) {
        data.playerTransfers.add(PlayerTransferRequest(playerID, mapName, code))
    }

    override fun kickPlayerFromMap(playerID: EntityID, code: ReturnCode) {
        data.playerTransfers.add(PlayerTransferRequest(playerID, null, code))
    }

    override fun gameOver(message: String?) {
        data.serverMessages.add(-1L to GameOverMessage(message))
    }

}