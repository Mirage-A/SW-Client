package com.mirage.gamelogic

import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.*
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.maps.SceneLoader
import com.mirage.utils.game.maps.ScriptArea
import com.mirage.utils.game.states.ExtendedState
import com.mirage.utils.game.states.SimplifiedState
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * State of a single game logic instance
 */
internal data class LogicData(
        val gameMapName: GameMapName,

        val serverMessages: Queue<Pair<EntityID, ServerMessage>> = ConcurrentLinkedQueue(),
        val playerTransfers: Queue<PlayerTransferRequest> = ConcurrentLinkedQueue(),

        /** Queue which stores new messages from clients */
        val clientMessages : Queue<Pair<EntityID, ClientMessage>> = ConcurrentLinkedQueue(),

        val sceneLoader: SceneLoader = SceneLoader(gameMapName),

        val gameMap : GameMap = sceneLoader.loadMap(),
        val state: ExtendedState = sceneLoader.loadInitialState(),
        val scriptAreas: Iterable<ScriptArea> = sceneLoader.loadAreas(),
        var latestStateSnapshot: SimplifiedState = state.simplifiedDeepCopy(),

        /** Maps id of player entity to list of his skills */
        val playerSkills: MutableMap<EntityID, SkillNames> = HashMap(),

        /** These maps must be mutated only through ScriptActions class */
        val playerGlobalQuestProgress: MutableMap<EntityID, QuestProgress> = HashMap(),
        val playerLocalQuestProgress: MutableMap<EntityID, QuestProgress> = HashMap(),

        /** Set of IDs of all players */
        val playerIDs: MutableSet<EntityID> = HashSet(),

        /**
         * Queue which stores new requests for new player creation.
         * After a player entity was created by logic loop, PlayerCreationListener is invoked.
         */
        val newPlayerRequests : Queue<PlayerCreationRequest> = ConcurrentLinkedQueue(),

        /** Queue which stores new requests for player removing. */
        val removePlayerRequests: Queue<EntityID> = ConcurrentLinkedQueue(),

        var initScriptInvoked: Boolean = false,

        /** Latest entity positions processed by [processScriptAreas] */
        var lastProcessedPositions: MutableMap<EntityID, Point> = HashMap(),

        /** Requests to invoke script at given time */
        val delayedScripts: PriorityQueue<Pair<TimeMillis, Pair<String, LuaTable>>> = PriorityQueue(compareBy {it.first} )

)