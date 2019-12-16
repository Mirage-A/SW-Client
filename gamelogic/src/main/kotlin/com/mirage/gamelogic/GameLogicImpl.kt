package com.mirage.gamelogic

import com.mirage.utils.Assets
import com.mirage.utils.GAME_LOOP_TICK_INTERVAL
import com.mirage.utils.Log
import com.mirage.utils.LoopTimer
import com.mirage.utils.extensions.*
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.maps.SceneLoader
import com.mirage.utils.game.states.ExtendedState
import com.mirage.utils.game.states.SimplifiedState
import com.mirage.utils.game.states.StateDifference
import com.mirage.utils.messaging.*
import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.JsePlatform
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.collections.ArrayList

class GameLogicImpl(private val gameMapName: String) : GameLogic {

    override val serverMessages: Queue<Pair<EntityID, ServerMessage>> = ConcurrentLinkedQueue()
    override val playerTransfers: Queue<PlayerTransferRequest> = ConcurrentLinkedQueue()

    /** Queue which stores new messages from clients */
    private val clientMessages : Queue<Pair<EntityID, ClientMessage>> = ConcurrentLinkedQueue()

    private val gameMap : GameMap = SceneLoader.loadMap(gameMapName)
    private val state: ExtendedState = SceneLoader.loadInitialState(gameMapName)
    private var latestStateSnapshot: SimplifiedState = state.simplifiedDeepCopy()

    /** Maps id of player entity to list of his skills */
    private val playerSkills: MutableMap<EntityID, SkillNames> = HashMap()

    /** These maps must be mutated only through ScriptActions class */
    private val playerGlobalQuestProgress: MutableMap<EntityID, QuestProgress> = HashMap()
    private val playerLocalQuestProgress: MutableMap<EntityID, QuestProgress> = HashMap()

    /** Set of IDs of all players */
    private val playerIDs: MutableSet<EntityID> = HashSet()

    /**
     * Queue which stores new requests for new player creation.
     * After a player entity was created by logic loop, PlayerCreationListener is invoked.
     */
    private val newPlayerRequests : Queue<Pair<QuestProgress?, PlayerCreationListener>> = ConcurrentLinkedQueue()

    /**
     * Updates game state, invoking [serverMessageListener] for new server messages.
     * @param delta Milliseconds passed from last invocation of this function.
     */
    private fun updateState(time: TimeMillis, delta: IntervalMillis) {
        if (delta > 200L) Log.i("Slow update: $delta ms")
        handleNewPlayerRequests(time, delta)
        handleClientMessages()
        processMoving(delta)
        invokeDelayedScripts(time)
        finishStateUpdate()
    }

    private fun getNewClientMessages() = ArrayList<Pair<EntityID, ClientMessage>>().apply {
        while (true) {
            val msg = clientMessages.poll()
            msg ?: break
            add(msg)
        }
    }

    private fun handleClientMessages() {
        for ((id, msg) in getNewClientMessages()) {
            if (msg !is MoveDirectionClientMessage && msg !is SetMovingClientMessage) println("Got client message $id $msg")
            when (msg) {
                is MoveDirectionClientMessage -> {
                    state.entities[id]?.moveDirection = msg.md
                }
                is SetMovingClientMessage -> {
                    state.entities[id]?.isMoving = msg.isMoving
                }
            }
        }
    }

    /** Process moving of all entities during [delta] interval, including collisions and entering script zones */
    private fun processMoving(delta: IntervalMillis) {
        //TODO Process script zones entering
        for ((_, entity) in state.entities) {
            //TODO Move this expression to Behavior class
            entity.action = if (entity.isMoving) "running" else "idle"
            if (entity.isMoving) {
                moveObject(entity, delta, gameMap, state)
            }
        }
    }

    private fun invokeDelayedScripts(currentTime: TimeMillis) {
        while (delayedScripts.isNotEmpty() && delayedScripts.peek().first < currentTime) {
            val (name, args) = delayedScripts.poll().second
            runAssetScript("scripts/$name", args)
        }
    }

    private fun handleNewPlayerRequests(currentTime: TimeMillis, deltaTime: IntervalMillis) {
        while (true) {
            val request = newPlayerRequests.poll() ?: break
            val player = createPlayer(gameMap)
            val id = state.addEntity(player)
            playerIDs.add(id)
            //TODO Загрузка информации о навыках игрока через сообщения при входе в игру и через хранение профиля в БД
            val skills = listOf("flame-strike", "flame-strike", "flame-strike", "flame-strike", "meteor")
            playerSkills[id] = skills
            playerGlobalQuestProgress[id] = request.first ?: QuestProgress()
            playerLocalQuestProgress[id] = QuestProgress()
            serverMessages.add(Pair(id, InitialGameStateMessage(
                    gameMapName, state.simplifiedDeepCopy(), id, currentTime - deltaTime
            )))
            request.second(id)
        }
    }

    private fun finishStateUpdate() {
        val finalState = state.simplifiedDeepCopy()
        val finalDifference = StateDifference(latestStateSnapshot, finalState)
        latestStateSnapshot = finalState
        serverMessages.add(Pair(-1L, GameStateUpdateMessage(finalDifference, System.currentTimeMillis())))
    }


    override fun addNewPlayer(globalQuestProgress: QuestProgress?, onComplete: PlayerCreationListener) {
        newPlayerRequests.add(globalQuestProgress to onComplete)
    }

    private val loopTimer = LoopTimer(GAME_LOOP_TICK_INTERVAL) { time, delta ->
        updateState(time, delta)
    }

    override fun startLogic() {
        serverMessages.add(Pair(-1L, InitialGameStateMessage(gameMapName, latestStateSnapshot, -1L, System.currentTimeMillis())))
        loopTimer.start()
    }

    override fun pauseLogic() = loopTimer.pause()

    override fun resumeLogic() = loopTimer.resume()

    override fun stopLogic() = loopTimer.stop()

    override fun dispose() {
        stopLogic()
        //TODO Safe exit for players
    }

    override fun handleMessage(id: EntityID, msg: ClientMessage) {
        clientMessages.add(Pair(id, msg))
    }

    private val delayedScripts = PriorityQueue<Pair<TimeMillis, Pair<String, LuaTable>>>(compareBy {it.first} )

    private val scriptActions = ScriptActions()
    private val coercedScriptActions = CoerceJavaToLua.coerce(scriptActions)

    /** Runs a script from file "assets/ASSET_NAME.lua" */
    private fun runAssetScript(assetName: String, args: LuaTable) {
        val globals = JsePlatform.standardGlobals()
        val chunk = globals.load(Assets.loadReader("$assetName.lua")?.readText() ?: "")
        args.set("actions", coercedScriptActions)
        chunk.call(args)
    }

    private inner class ScriptActions : LogicScriptActions {

        override fun runLogicScript(scriptName: String, args: LuaTable) {
            runAssetScript("scripts/$scriptName", args)
        }

        override fun runLogicScriptAfterTimeout(scriptName: String, args: LuaTable, timeout: Long) {
            delayedScripts.add((System.currentTimeMillis() + timeout) to (scriptName to args))
        }

        override fun getState(): ExtendedState = state

        override fun getGameMap(): GameMap = gameMap

        override fun getGameMapName(): String = gameMapName

        override fun findEntity(name: String): EntityID? = state.entities.entries.find { it.value.name == name }?.key

        override fun findBuilding(name: String): BuildingID? = state.buildings.entries.find { it.value.name == name }?.key

        override fun findAllEntities(name: String): List<EntityID> =
                state.entities.filter { it.value.name == name }.map { it.key }

        override fun findAllBuildings(name: String): List<BuildingID> =
                state.buildings.filter { it.value.name == name }.map { it.key }

        override fun findAllPlayers(): List<EntityID> = playerIDs.toList()

        override fun print(msg: Any?) = Log.i(msg)

        override fun setSkillCooldown(playerID: EntityID, skillID: Int, cooldown: Float) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getSkillCooldown(playerID: EntityID, skillID: Int): Float {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun startDialog(playerID: EntityID, dialogName: String) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun sendTextMessage(playerID: EntityID, message: String) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun sendTextMessageToAll(message: String) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getGlobalQuestProgress(playerID: EntityID): QuestProgress? =
                playerGlobalQuestProgress[playerID]

        override fun getLocalQuestProgress(playerID: EntityID): QuestProgress? =
                playerLocalQuestProgress[playerID]

        override fun setGlobalQuestPhase(playerID: EntityID, questName: String, newPhase: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun setLocalQuestPhase(playerID: EntityID, questName: String, newPhase: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun sendPlayerToAnotherMap(playerID: EntityID, mapName: String, code: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun kickPlayerFromMap(playerID: EntityID, code: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

}