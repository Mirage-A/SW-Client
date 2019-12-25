package com.mirage.gamelogic

import com.mirage.gamelogic.logic.*
import com.mirage.gamelogic.logic.handleClientMessages
import com.mirage.gamelogic.logic.invokeDelayedScripts
import com.mirage.gamelogic.logic.moveObject
import com.mirage.gamelogic.logic.runAssetScript
import com.mirage.utils.Assets
import com.mirage.utils.GAME_LOOP_TICK_INTERVAL
import com.mirage.utils.Log
import com.mirage.utils.LoopTimer
import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.rangeBetween
import com.mirage.utils.extensions.*
import com.mirage.utils.game.objects.extended.ExtendedEntity
import com.mirage.utils.game.states.StateDifference
import com.mirage.utils.messaging.*
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import java.util.*
import kotlin.collections.ArrayList

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
            if (!initScriptInvoked) {
                runAssetScript("scenes/$gameMapName/init", tableOf(), scriptActions.coerced)
                initScriptInvoked = true
            }
            handleNewPlayerRequests(time, delta, scriptActions.coerced)
            handleRemovePlayerRequests(scriptActions.coerced)
            handleClientMessages(scriptActions.coerced)
            processMoving(delta)
            processScriptAreas(scriptActions.coerced)
            invokeDelayedScripts(time, scriptActions.coerced)
            finishStateUpdate()
        }
    }

    //TODO Move to Behavior class
    /** Process moving of all entities during [delta] interval, including collisions and entering script zones */
    private fun LogicData.processMoving(delta: IntervalMillis) {
        //TODO Process script zones entering
        for ((_, entity) in state.entities) {
            //TODO Move this expression to Behavior class
            entity.action = if (entity.isMoving) "running" else "idle"
            if (entity.isMoving) {
                moveObject(entity, delta, gameMap, state)
            }
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