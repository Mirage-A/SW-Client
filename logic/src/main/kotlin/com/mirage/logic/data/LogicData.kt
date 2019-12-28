package com.mirage.logic.data

import com.mirage.core.datastructures.Point
import com.mirage.core.datastructures.Rectangle
import com.mirage.core.extensions.*
import com.mirage.core.game.maps.GameMap
import com.mirage.core.game.states.ExtendedState
import com.mirage.core.game.states.SimplifiedState
import com.mirage.core.messaging.ClientMessage
import com.mirage.core.messaging.ServerMessage
import com.mirage.logic.behavior.Behavior
import org.luaj.vm2.LuaTable
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
        val clientMessages: Queue<Pair<EntityID, ClientMessage>> = ConcurrentLinkedQueue(),

        val sceneLoader: ExtendedSceneLoader = ExtendedSceneLoader(gameMapName),

        val gameMap: GameMap = sceneLoader.loadMap(),
        val state: ExtendedState = sceneLoader.loadInitialState(),
        val scriptAreas: Iterable<ScriptArea> = sceneLoader.loadAreas(),
        var latestStateSnapshot: SimplifiedState = state.simplifiedDeepCopy(),

        val behaviors: MutableMap<EntityID, Behavior> = HashMap(),

        /** These maps must be mutated only through ScriptActions class */
        val playerGlobalQuestProgress: MutableMap<EntityID, QuestProgress> = HashMap(),
        val playerLocalQuestProgress: MutableMap<EntityID, QuestProgress> = HashMap(),

        /** Set of IDs of all players */
        val playerIDs: MutableSet<EntityID> = HashSet(),

        /**
         * Queue which stores new requests for new player creation.
         * After a player entity was created by logic loop, PlayerCreationListener is invoked.
         */
        val newPlayerRequests: Queue<PlayerCreationRequest> = ConcurrentLinkedQueue(),

        /** Queue which stores new requests for player removing. */
        val removePlayerRequests: Queue<EntityID> = ConcurrentLinkedQueue(),

        var initScriptInvoked: Boolean = false,

        /** Latest entity positions processed by [processScriptAreas] */
        var lastProcessedPositions: MutableMap<EntityID, Point> = HashMap(),

        /** Requests to invoke script at given time */
        val delayedScripts: PriorityQueue<DelayedScriptRequest> = PriorityQueue()

)

data class PlayerTransferRequest(val entityID: EntityID, val gameMapName: GameMapName?, val returnCode: ReturnCode)

internal data class ScriptArea(val area: Rectangle, val playersOnly: Boolean, val onEnter: String?, val onLeave: String?)

internal data class DelayedScriptRequest(
        val invocationTime: TimeMillis, val scriptName: String, val args: LuaTable
) : Comparable<DelayedScriptRequest> {

    override fun compareTo(other: DelayedScriptRequest): Int =
            this.invocationTime.compareTo(other.invocationTime)

}