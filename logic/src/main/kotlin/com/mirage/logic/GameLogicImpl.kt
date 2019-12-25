package com.mirage.logic

import com.mirage.logic.processors.*
import com.mirage.logic.processors.processClientMessages
import com.mirage.logic.processors.invokeDelayedScripts
import com.mirage.logic.processors.moveObject
import com.mirage.logic.processors.runAssetScript
import com.mirage.core.GAME_LOOP_TICK_INTERVAL
import com.mirage.core.Log
import com.mirage.core.LoopTimer
import com.mirage.core.extensions.*
import com.mirage.core.game.states.StateDifference
import com.mirage.core.messaging.*
import java.util.*

class GameLogicImpl(gameMapName: GameMapName) : GameLogic {

    private val data = LogicData(gameMapName)
    private val scriptActions: LogicScriptActions = LogicScriptActionsImpl(data)

    override val playerTransfers: Queue<PlayerTransferRequest> = data.playerTransfers
    override val serverMessages: Queue<Pair<EntityID, ServerMessage>> = data.serverMessages

    /**
     * Updates game state, filling [serverMessages] and [playerTransfers] queues.
     * @param delta Milliseconds passed from last invocation of this function.
     */
    private fun updateState(time: TimeMillis, delta: IntervalMillis) {
        if (delta > 200L) Log.i("Slow update: $delta ms")
        with (data) {
            triggerInitScript()
            processNewPlayerRequests(time, delta, scriptActions.coerced)
            processRemovePlayerRequests(scriptActions.coerced)
            processClientMessages(scriptActions.coerced)
            processBehaviors(delta)
            processScriptAreas(scriptActions.coerced)
            invokeDelayedScripts(time, scriptActions.coerced)
            finishStateUpdate()
        }
    }

    private fun LogicData.triggerInitScript() {
        if (!initScriptInvoked) {
            runAssetScript("scenes/$gameMapName/init", tableOf(), scriptActions.coerced)
            initScriptInvoked = true
        }
    }

    private fun LogicData.processBehaviors(delta: IntervalMillis) {
        for ((entityID, entity) in state.entities) {
            val savedBehavior = behaviors[entityID]
            val behavior = if (savedBehavior == null) {
                val loadedBehavior = sceneLoader.loadBehavior(entity.template)
                behaviors[entityID] = loadedBehavior
                loadedBehavior
            }
            else savedBehavior
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

    private val loopTimer = LoopTimer(GAME_LOOP_TICK_INTERVAL) { time, delta ->
        updateState(time, delta)
    }

    override fun startLogic(initialPlayerRequests: Iterable<PlayerCreationRequest>) {
        with (data) {
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