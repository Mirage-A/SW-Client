package com.mirage.logic

import com.mirage.core.extensions.EntityID
import com.mirage.core.game.maps.GameMap
import com.mirage.core.game.objects.extended.ExtendedBuilding
import com.mirage.core.game.objects.extended.ExtendedEntity
import com.mirage.core.game.states.ExtendedState
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

/** Methods which can be used by any script */
internal interface LogicScriptActions {

    /** Instance of this interface, coerced to lua value */
    val coerced: LuaValue

    /** Runs another script from scripts folder of this scene immediately */
    fun runSceneScript(scriptName: String, args: LuaTable)

    /** Runs another script from scripts folder of this scene after timeout. This script will still be invoked in logic thread */
    fun runSceneScriptAfterTimeout(scriptName: String, args: LuaTable, timeout: Long)

    /** Returns whole state of the game */
    fun getState(): ExtendedState

    /** Returns game map */
    fun getGameMap(): GameMap
    /** Returns game map name */
    fun getGameMapName(): String

    /** Finds any entity with the name [name] */
    fun findEntityID(name: String): Long?
    /** Finds any building with the name [name] */
    fun findBuildingID(name: String): Long?

    fun findEntity(name: String): ExtendedEntity?
    fun findBuilding(name: String): ExtendedBuilding?

    fun getEntity(id: EntityID): ExtendedEntity?

    /** Finds all entities with the name [name] */
    fun findAllEntityIDs(name: String): List<Long>
    /** Finds all buildings with the name [name] */
    fun findAllBuildingIDs(name: String): List<Long>

    /** Returns table with all player IDs */
    fun findAllPlayerIDs(): List<Long>

    /** Adds [entity] to scene and returns its id */
    fun addEntity(entity: ExtendedEntity): EntityID

    /** Deals damage from [sourceID] entity (use -1 for no source) to [entityID] entity, maybe triggering death script of [targetID] */
    fun dealDamageToEntity(sourceID: EntityID, entityID: EntityID, damage: Int)

    /** Removes entity [entityID] from scene, triggering its destroy script */
    fun destroyEntity(entityID: EntityID)

    /** Logs an object [msg] using standard logger */
    fun print(msg: Any?)

    /** Sets a cooldown for player's skill */
    fun setSkillCooldown(playerID: EntityID, skillID: Int, cooldown: Float)
    /** Returns current cooldown of player's skill */
    fun getSkillCooldown(playerID: EntityID, skillID: Int): Float

    /** Starts a dialog [dialogName] for player [playerID] */
    fun startDialog(playerID: EntityID, dialogName: String)

    /** Sends a text message to player. It will be displayed as a small text on top of player's screen */
    fun sendTextMessage(playerID: EntityID, message: String)
    /** Sends a text message to all players in this room */
    fun sendTextMessageToAll(message: String)

    /** Returns player's current phase in global quest [questName]. Returns 0 by default (if quest was not started yet). */
    fun getGlobalQuestPhase(playerID: EntityID, questName: String): Int
    /** Returns player's current phase in local quest [questName]. Returns 0 by default (if quest was not started yet). */
    fun getLocalQuestPhase(playerID: EntityID, questName: String): Int

    /** Sets the current phase of global quest [questName] for player [playerID] to [newPhase] and sends message about it to this player. */
    fun setGlobalQuestPhase(playerID: EntityID, questName: String, newPhase: Int)
    /** Sets the current phase of local quest [questName] for player [playerID] to [newPhase] and sends message about it to this player. */
    fun setLocalQuestPhase(playerID: EntityID, questName: String, newPhase: Int)

    /** Sends a player [playerID] to map [mapName] with "return code" [code] (e.g. 0 for successful mission, -1 for death etc) */
    fun sendPlayerToAnotherMap(playerID: EntityID, mapName: String, code: Int)
    /** Removes a player [playerID] from this map with "return code" [code] */
    fun kickPlayerFromMap(playerID: EntityID, code: Int)

}