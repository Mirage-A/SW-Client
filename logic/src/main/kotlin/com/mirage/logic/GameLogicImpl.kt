package com.mirage.logic

import com.mirage.core.game.objects.StateDifference
import com.mirage.core.messaging.ClientMessage
import com.mirage.core.messaging.GameStateUpdateMessage
import com.mirage.core.messaging.InitialGameStateMessage
import com.mirage.core.messaging.ServerMessage
import com.mirage.core.utils.*
import com.mirage.logic.data.LogicData
import com.mirage.logic.data.PlayerTransferRequest
import com.mirage.logic.processors.*
import java.util.*

class GameLogicImpl(gameMapName: GameMapName) : GameLogic {

    private val data = LogicData(gameMapName)
    private val scriptActions: LogicScriptActions = LogicScriptActionsImpl(data)

    override val playerTransfers: Queue<PlayerTransferRequest> = data.playerTransfers
    override val serverMessages: Queue<Pair<EntityID, ServerMessage>> = data.serverMessages

    private val loopTimer = LoopTimer(GAME_LOOP_TICK_INTERVAL) { time, delta -> updateState(time, delta) }

    /**
     * Updates game state, filling [serverMessages] and [playerTransfers] queues.
     * @param delta Milliseconds passed from last invocation of this function.
     */
    private fun updateState(time: TimeMillis, delta: IntervalMillis) {
        if (delta > 200L) Log.i("Slow update: $delta ms")
        with(data) {
            triggerInitScript()
            processNewPlayerRequests(time, delta, scriptActions.coerced)
            processRemovePlayerRequests(scriptActions.coerced)
            processClientMessages(scriptActions.coerced)
            processBehaviors(delta)
            processScriptAreas(scriptActions.coerced)
            invokeDelayedScripts(time, scriptActions.coerced)
            finishStateUpdate()
            triggerAllDeadScript()
        }
    }

    private fun LogicData.triggerInitScript() {
        if (!initScriptInvoked) {
            runAssetScript("scenes/$gameMapName/init", tableOf(), scriptActions.coerced)
            initScriptInvoked = true
        }
    }

    private fun LogicData.triggerAllDeadScript() {
        if (playerIDs.map { state.entities[it] }.all { it?.state ?: "dead" == "dead" }) {
            runAssetScript("scenes/$gameMapName/all-players-dead", tableOf(), scriptActions.coerced)
        }
    }

    private fun LogicData.processBehaviors(delta: IntervalMillis) {
        val entityIDs = state.entities.keys.toList()
        for (entityID in entityIDs) {
            val entity = state.entities[entityID] ?: continue
            val savedBehavior = behaviors[entityID]
            val behavior = if (savedBehavior == null) {
                val loadedBehavior = sceneLoader.loadBehavior(entity.template, entityID, data)
                behaviors[entityID] = loadedBehavior
                loadedBehavior
            } else savedBehavior
            behavior.onUpdate(delta, data, scriptActions.coerced)
        }
    }

    private fun LogicData.finishStateUpdate() {
        val finalState = state.simplifiedDeepCopy()
        val finalDifference = StateDifference(latestStateSnapshot, finalState)
        latestStateSnapshot = finalState
        serverMessages.add(Pair(-1L, GameStateUpdateMessage(finalDifference, System.currentTimeMillis())))
    }


    override fun addNewPlayer(request: PlayerCreationRequest) {
        data.newPlayerRequests.add(request)
    }

    override fun removePlayer(playerID: EntityID) {
        data.removePlayerRequests.add(playerID)
    }

    override fun startLogic(initialPlayerRequests: Iterable<PlayerCreationRequest>) {
        with(data) {
            for ((id, entity) in state.entities) {
                behaviors[id] = sceneLoader.loadBehavior(entity.template, id, this)
            }
            serverMessages.add(Pair(-1L, InitialGameStateMessage(gameMapName, latestStateSnapshot, -1L, System.currentTimeMillis())))
            initialPlayerRequests.forEach { newPlayerRequests.add(it) }
            loopTimer.start()
        }
    }

    override fun pauseLogic() = loopTimer.pause()

    override fun resumeLogic() = loopTimer.resume()

    override fun stopLogic() = loopTimer.stop()

    override fun dispose() {
        stopLogic()
        //TODO Safe exit for players
    }

    override fun handleMessage(id: EntityID, msg: ClientMessage) {
        data.clientMessages.add(Pair(id, msg))
    }


}