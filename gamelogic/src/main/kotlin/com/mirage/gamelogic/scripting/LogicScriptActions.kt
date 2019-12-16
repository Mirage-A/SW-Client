package com.mirage.gamelogic.scripting

import com.mirage.utils.extensions.EntityID
import com.mirage.utils.extensions.QuestProgress
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.objects.extended.ExtendedBuilding
import com.mirage.utils.game.objects.extended.ExtendedEntity
import com.mirage.utils.game.objects.simplified.SimplifiedBuilding
import com.mirage.utils.game.objects.simplified.SimplifiedEntity
import com.mirage.utils.game.objects.simplified.SimplifiedObject
import com.mirage.utils.game.states.ExtendedState
import org.luaj.vm2.LuaDouble
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

/**
 * Интерфейс, который определяет методы, которыми может пользоваться вызываемый скрипт.
 */
interface LogicScriptActions {

    /** Runs another script from scripts folder immediately */
    fun runLogicScript(scriptName: String, args: LuaTable)

    /** Runs another script from scripts folder after timeout. This script will still be invoked in logic thread */
    fun runLogicScriptAfterTimeout(scriptName: String, args: LuaTable, timeout: Long)

    /** Returns whole state of the game */
    fun getState(): ExtendedState

    /** Returns game map */
    fun getGameMap(): GameMap
    /** Returns game map name */
    fun getGameMapName(): String

    /** Finds any entity with the name [name] */
    fun findEntity(name: String): Long?
    /** Finds any building with the name [name] */
    fun findBuilding(name: String): Long?

    /** Finds all entities with the name [name] */
    fun findAllEntities(name: String) : List<Long>
    /** Finds all buildings with the name [name] */
    fun findAllBuildings(name: String) : List<Long>

    /** Returns table with all player IDs */
    fun findAllPlayers() : List<Long>

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

    /** Returns a map with player's global quest progress. This map should not be changed in script. */
    fun getGlobalQuestProgress(playerID: EntityID): QuestProgress
    /** Returns a map with player's local quest progress. This map should not be changed in script. */
    fun getLocalQuestProgress(playerID: EntityID): QuestProgress

    /** Sets the current phase of global quest [questName] for player [playerID] to [newPhase] and sends message about it to this player. */
    fun setGlobalQuestPhase(playerID: EntityID, questName: String, newPhase: Int)
    /** Sets the current phase of local quest [questName] for player [playerID] to [newPhase] and sends message about it to this player. */
    fun setLocalQuestPhase(playerID: EntityID, questName: String, newPhase: Int)

    /** Sends a player [playerID] to map [mapName] with "return code" [code] (e.g. 0 for successful mission, -1 for death etc) */
    fun sendPlayerToAnotherMap(playerID: EntityID, mapName: String, code: Int)
    /** Removes a player [playerID] from this map with "return code" [code] */
    fun kickPlayerFromMap(playerID: EntityID, code: Int)

}