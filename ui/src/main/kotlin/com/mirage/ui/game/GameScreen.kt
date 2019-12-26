package com.mirage.ui.game

import com.mirage.view.GameViewImpl
import com.mirage.view.utils.getVirtualScreenPointFromScene
import com.mirage.ui.Screen
import com.mirage.core.DELTA_CENTER_Y
import com.mirage.core.PLATFORM
import com.mirage.core.TestSamples
import com.mirage.core.datastructures.Point
import com.mirage.core.extensions.GameMapName
import com.mirage.core.game.maps.GameMap
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.game.objects.simplified.SimplifiedEntity
import com.mirage.core.game.states.GameStateSnapshot
import com.mirage.core.game.states.SimplifiedState
import com.mirage.core.game.states.SnapshotManager
import com.mirage.core.game.states.StateDifference
import com.mirage.core.messaging.*
import com.mirage.core.preferences.Prefs
import com.mirage.core.virtualscreen.VirtualScreen
import rx.Observable
import kotlin.math.min

class GameScreen(gameMapName: GameMapName, gameMap: GameMap, virtualScreen: VirtualScreen) : Screen {

    private val uiState : GameUIState = GameUIState(virtualScreen, gameMapName)

    override val inputProcessor : GameInputProcessor = when (PLATFORM) {
        "desktop", "test" -> DesktopGameInputProcessor(uiState)
        else -> DesktopGameInputProcessor(uiState)
    }

    private val uiRenderer : GameUIRenderer = when (PLATFORM) {
        "desktop", "test" -> DesktopGameUIRenderer()
        else -> DesktopGameUIRenderer()
    }

    private val gameView = GameViewImpl(gameMapName, gameMap)

    private val snapshotManager = SnapshotManager()

    private var lastReceivedState : SimplifiedState = TestSamples.TEST_NO_GAME_OBJECTS

    override fun handleServerMessage(msg: ServerMessage) {
        when (msg) {
            is InitialGameStateMessage -> {
                gameView.loadDrawers(msg.initialState)
                println("Loading drawers for initial state: ${msg.initialState}")
                snapshotManager.addNewSnapshot(GameStateSnapshot(msg.initialState, StateDifference(), msg.stateCreatedTimeMillis))
                uiState.playerID = msg.playerID
                lastReceivedState = msg.initialState
            }
            is GameStateUpdateMessage -> {
                gameView.updateDrawers(lastReceivedState, msg.diff)
                val state = msg.diff.projectOn(lastReceivedState)
                lastReceivedState = state
                snapshotManager.addNewSnapshot(GameStateSnapshot(state, msg.diff, msg.stateCreatedTimeMillis))
            }
            is LocalQuestUpdateMessage -> {
                //TODO Show notification about quest progress
                uiState.localQuestProgress[msg.localQuestName] = msg.newPhaseID
                inputProcessor.updateQuestWindow()
            }
            is GlobalQuestUpdateMessage -> {
                //TODO Show notification about quest progress
                Prefs.profile.globalQuestProgress[msg.globalQuestName] = msg.newPhaseID
                inputProcessor.updateQuestWindow()
            }
            is HumanoidEquipmentUpdateMessage -> {
                gameView.setHumanoidEquipment(
                        msg.objectID,
                        lastReceivedState.entities[msg.objectID] ?: SimplifiedEntity(),
                        msg.newEquipment
                )
            }
            is GameOverMessage -> {
                //TODO
                inputProcessor.inputMessages.onNext(CloseConnectionMessage())
                with (uiState) {
                    gameOver = true
                    gameOverStartTime = System.currentTimeMillis()
                    gameOverMessage.boundedLabel?.text = msg.message ?: ""
                    gameOverMessage.isVisible = msg.message != null
                    widgets.forEach { it.isVisible = false }
                }
            }
        }
    }

    override fun render(virtualScreen: VirtualScreen, currentTimeMillis: Long) {
        uiState.let {
            if (it.bufferedMoving != it.lastSentMoving) {
                it.bufferedMoving?.let { newMoving ->
                    uiState.lastSentMoving = newMoving
                    inputProcessor.inputMessages.onNext(SetMovingClientMessage(newMoving))
                }
            }
            if (it.bufferedMoveDirection != it.lastSentMoveDirection) {
                it.bufferedMoveDirection?.let { newMoveDirection ->
                    uiState.lastSentMoveDirection = newMoveDirection
                    inputProcessor.inputMessages.onNext(MoveDirectionClientMessage(when (newMoveDirection) {
                        MoveDirection.RIGHT -> MoveDirection.UP_RIGHT
                        MoveDirection.UP_RIGHT -> MoveDirection.UP
                        MoveDirection.UP -> MoveDirection.UP_LEFT
                        MoveDirection.UP_LEFT -> MoveDirection.LEFT
                        MoveDirection.LEFT -> MoveDirection.DOWN_LEFT
                        MoveDirection.DOWN_LEFT -> MoveDirection.DOWN
                        MoveDirection.DOWN -> MoveDirection.DOWN_RIGHT
                        MoveDirection.DOWN_RIGHT -> MoveDirection.RIGHT
                    }))
                }
            }
        }
        val state = snapshotManager.getInterpolatedSnapshot(currentTimeMillis)
        gameView.renderGameState(virtualScreen,
                state,
                state.entities[uiState.playerID]?.position ?: Point(0f, 0f),
                uiState.targetID,
                state.entities[uiState.playerID]?.factionID != state.entities[uiState.targetID]?.factionID
        )
        if (uiState.gameOver) {
            val timePassed = currentTimeMillis - uiState.gameOverStartTime
            val alpha = min(1f, timePassed.toFloat() / screenFadingInterval.toFloat()) * maxFadingAlpha
            virtualScreen.drawColorOnAllScreen(0f, 0f, 0f, alpha)
            if (timePassed > screenFadingInterval) {
                uiState.gameOverCompositeWidget.isVisible = true
            }
        }
        uiRenderer.renderUI(virtualScreen, uiState, currentTimeMillis)
        uiState.lastRenderedState = state
    }

    fun changeTarget(virtualHitPoint: Point) {
        val player = uiState.lastRenderedState.entities[uiState.playerID] ?: return
        val playerOnVirtualScreen = getVirtualScreenPointFromScene(player.position)
        val virtualPoint = Point(virtualHitPoint.x + playerOnVirtualScreen.x, virtualHitPoint.y + playerOnVirtualScreen.y + DELTA_CENTER_Y)
        val targetID = gameView.hit(virtualPoint, uiState.lastRenderedState)
        if (targetID != uiState.targetID) inputProcessor.inputMessages.onNext(SetTargetClientMessage(targetID))
        if (targetID != null) uiState.targetID = targetID
    }

    fun clearTarget() {
        uiState.targetID = null
    }

    override val inputMessages: Observable<ClientMessage> = inputProcessor.inputMessages

    override fun resize(virtualWidth: Float, virtualHeight: Float) = uiState.resize(virtualWidth, virtualHeight)

}