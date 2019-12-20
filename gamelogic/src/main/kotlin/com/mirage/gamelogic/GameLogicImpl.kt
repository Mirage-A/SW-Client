package com.mirage.gamelogic

import com.mirage.utils.Assets
import com.mirage.utils.GAME_LOOP_TICK_INTERVAL
import com.mirage.utils.Log
import com.mirage.utils.LoopTimer
import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.rangeBetween
import com.mirage.utils.extensions.*
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.maps.SceneLoader
import com.mirage.utils.game.maps.ScriptArea
import com.mirage.utils.game.objects.extended.ExtendedBuilding
import com.mirage.utils.game.objects.extended.ExtendedEntity
import com.mirage.utils.game.states.ExtendedState
import com.mirage.utils.game.states.SimplifiedState
import com.mirage.utils.game.states.StateDifference
import com.mirage.utils.messaging.*
import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.collections.ArrayList

class GameLogicImpl(private val gameMapName: GameMapName) : GameLogic {

    override val serverMessages: Queue<Pair<EntityID, ServerMessage>> = ConcurrentLinkedQueue()
    override val playerTransfers: Queue<PlayerTransferRequest> = ConcurrentLinkedQueue()

    /** Queue which stores new messages from clients */
    private val clientMessages : Queue<Pair<EntityID, ClientMessage>> = ConcurrentLinkedQueue()

    private val sceneLoader = SceneLoader(gameMapName)

    private val gameMap : GameMap = sceneLoader.loadMap()
    private val state: ExtendedState = sceneLoader.loadInitialState()
    private val scriptAreas: Iterable<ScriptArea> = sceneLoader.loadAreas()
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

    /** Queue which stores new requests for player removing. */
    private val removePlayerRequests: Queue<EntityID> = ConcurrentLinkedQueue()

    private var initScriptInvoked = false

    /**
     * Updates game state, filling [serverMessages] and [playerTransfers] queues.
     * @param delta Milliseconds passed from last invocation of this function.
     */
    private fun updateState(time: TimeMillis, delta: IntervalMillis) {
        if (delta > 200L) Log.i("Slow update: $delta ms")
        if (!initScriptInvoked) {
            runAssetScript("scenes/$gameMapName/init", tableOf())
            initScriptInvoked = true
        }
        handleRemovePlayerRequests()
        handleClientMessages()
        processMoving(delta)
        processScriptAreas()
        invokeDelayedScripts(time)
        handleNewPlayerRequests(time, delta)
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
            when (msg) {
                is MoveDirectionClientMessage -> {
                    state.entities[id]?.moveDirection = msg.md
                }
                is SetMovingClientMessage -> {
                    state.entities[id]?.isMoving = msg.isMoving
                }
                is CastSkillClientMessage -> {
                    //TODO Skill casting
                }
                is InteractionClientMessage -> {
                    val player = state.entities[id]
                    val entity = state.entities[msg.entityID]
                    if (player != null && entity != null && rangeBetween(player.position, entity.position) < entity.interactionRange) {
                        runAssetScript(
                                "scenes/$gameMapName/templates/entities/${entity.template}/interaction",
                                tableOf("playerID" to id, "entityID" to msg.entityID)
                        )
                    }
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

    /** Latest entity positions processed by [processScriptAreas] */
    private var lastProcessedPositions: MutableMap<EntityID, Point> = HashMap()

    /** Process entering and leaving script areas */
    private fun processScriptAreas() {
        for ((id, entity) in state.entities) {
            val isPlayer = id in playerIDs
            val lastPosition = lastProcessedPositions[id] ?: Point(Float.MAX_VALUE, Float.MAX_VALUE)
            for (area in scriptAreas) {
                if (isPlayer || !area.playersOnly) {
                    val wasInArea = lastPosition in area.area
                    val isInArea = entity.position in area.area
                    if (wasInArea != isInArea) {
                        val scriptName = if (isInArea) area.onEnter else area.onLeave
                        scriptName ?: continue
                        runAssetScript("scenes/$gameMapName/areas/$scriptName", tableOf("entityID" to id))
                    }
                }
            }
            lastProcessedPositions[id] = entity.position
        }
    }

    /** Requests to invoke script at given time */
    private val delayedScripts = PriorityQueue<Pair<TimeMillis, Pair<String, LuaTable>>>(compareBy {it.first} )

    private fun invokeDelayedScripts(currentTime: TimeMillis) {
        while (delayedScripts.isNotEmpty() && delayedScripts.peek().first < currentTime) {
            val (name, args) = delayedScripts.poll().second
            runAssetScript("scenes/$gameMapName/scripts/$name", args)
        }
    }

    private fun createPlayer(): ExtendedEntity = sceneLoader.loadEntityTemplate("player").with(
            x = gameMap.spawnX,
            y = gameMap.spawnY
    )

    private fun handleNewPlayerRequests(currentTime: TimeMillis, deltaTime: IntervalMillis) {
        newPlayerRequests.processNewItems {
            val player = createPlayer()
            val id = state.addEntity(player)
            playerIDs.add(id)
            //TODO Загрузка информации о навыках игрока через сообщения при входе в игру и через хранение профиля в БД
            val skills = listOf("flame-strike", "flame-strike", "flame-strike", "flame-strike", "meteor")
            playerSkills[id] = skills
            playerGlobalQuestProgress[id] = it.first ?: QuestProgress()
            playerLocalQuestProgress[id] = QuestProgress()
            serverMessages.add(Pair(id, InitialGameStateMessage(
                    gameMapName, state.simplifiedDeepCopy(), id, currentTime - deltaTime
            )))
            runAssetScript("scenes/$gameMapName/new-player", tableOf("playerID" to id))
            it.second(id)
        }
    }

    private fun handleRemovePlayerRequests() {
            removePlayerRequests.processNewItems {
                runAssetScript("scenes/$gameMapName/player-exit", tableOf("playerID" to it))
                state.removeEntity(it)
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

    override fun removePlayer(playerID: EntityID) {
        removePlayerRequests.add(playerID)
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


    private val scriptActions = ScriptActions()
    private val coercedScriptActions = CoerceJavaToLua.coerce(scriptActions)

    /** Runs a script from file "assets/ASSET_NAME.lua" */
    private fun runAssetScript(assetName: String, args: LuaTable) {
        args.set("actions", coercedScriptActions)
        val reader = Assets.loadReader("$assetName.lua") ?: return
        runScript(reader, args)
    }

    private inner class ScriptActions : LogicScriptActions {

        override fun runSceneScript(scriptName: String, args: LuaTable) {
            runAssetScript("scenes/$gameMapName/scripts/$scriptName", args)
        }

        override fun runSceneScriptAfterTimeout(scriptName: String, args: LuaTable, timeout: Long) {
            delayedScripts.add((System.currentTimeMillis() + timeout) to (scriptName to args))
        }

        override fun getState(): ExtendedState = state

        override fun getGameMap(): GameMap = gameMap

        override fun getGameMapName(): String = gameMapName

        override fun findEntityID(name: String): EntityID? = state.entities.entries.find { it.value.name == name }?.key

        override fun findBuildingID(name: String): BuildingID? = state.buildings.entries.find { it.value.name == name }?.key

        override fun findEntity(name: String): ExtendedEntity? = state.entities.entries.find { it.value.name == name }?.value

        override fun findBuilding(name: String): ExtendedBuilding? = state.buildings.entries.find { it.value.name == name }?.value

        override fun findAllEntityIDs(name: String): List<EntityID> =
                state.entities.filter { it.value.name == name }.map { it.key }

        override fun findAllBuildingIDs(name: String): List<BuildingID> =
                state.buildings.filter { it.value.name == name }.map { it.key }

        override fun findAllPlayerIDs(): List<EntityID> = playerIDs.toList()

        override fun dealDamageToEntity(sourceID: EntityID, entityID: EntityID, damage: Int) {
            state.entities[entityID]?.let {
                it.health -= damage
                if (it.health <= 0) {
                    it.health = 0
                    runAssetScript(
                            "scenes/$gameMapName/templates/entities/${it.template}/death",
                            tableOf("sourceID" to sourceID, "entityID" to entityID)
                    )
                }
            }
        }

        override fun destroyEntity(entityID: EntityID) {
            val entity = state.entities[entityID] ?: return
            runAssetScript(
                    "scenes/$gameMapName/templates/entities/${entity.template}/destroy",
                    tableOf("entityID" to entityID)
            )
            state.removeEntity(entityID)
        }

        override fun print(msg: Any?) = Log.i(msg)

        override fun setSkillCooldown(playerID: EntityID, skillID: Int, cooldown: Float) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getSkillCooldown(playerID: EntityID, skillID: Int): Float {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun startDialog(playerID: EntityID, dialogName: String) {
            serverMessages.add(Pair(playerID, StartDialogMessage(dialogName)))
        }

        override fun sendTextMessage(playerID: EntityID, message: String) {
            serverMessages.add(Pair(playerID, DisplayTextMessage(message)))
        }

        override fun sendTextMessageToAll(message: String) {
            serverMessages.add(Pair(-1L, DisplayTextMessage(message)))
        }

        override fun getGlobalQuestPhase(playerID: EntityID, questName: String): Int =
                playerGlobalQuestProgress[playerID]?.getOrDefault(questName, 0) ?: 0

        override fun getLocalQuestPhase(playerID: EntityID, questName: String): Int =
                playerLocalQuestProgress[playerID]?.getOrDefault(questName, 0) ?: 0

        override fun setGlobalQuestPhase(playerID: EntityID, questName: String, newPhase: Int) {
            playerGlobalQuestProgress[playerID]?.let {
                it[questName] = newPhase
                serverMessages.add(Pair(playerID, GlobalQuestUpdateMessage(questName, newPhase)))
                val args = tableOf("playerID" to playerID, "phase" to newPhase)
                runAssetScript("global-quests/$questName/update", args)
            }
        }

        override fun setLocalQuestPhase(playerID: EntityID, questName: String, newPhase: Int) {
            playerLocalQuestProgress[playerID]?.let {
                it[questName] = newPhase
                serverMessages.add(Pair(playerID, LocalQuestUpdateMessage(questName, newPhase)))
                val args = tableOf("playerID" to playerID, "phase" to newPhase)
                runAssetScript("scenes/$gameMapName/quests/$questName/update", args)
            }
        }

        override fun sendPlayerToAnotherMap(playerID: EntityID, mapName: GameMapName, code: ReturnCode) {
            playerTransfers.add(PlayerTransferRequest(playerID, mapName, code))
        }

        override fun kickPlayerFromMap(playerID: EntityID, code: ReturnCode) {
            playerTransfers.add(PlayerTransferRequest(playerID, null, code))
        }

    }

}