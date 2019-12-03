package com.mirage.ui.game

import com.mirage.gameview.GameViewImpl
import com.mirage.ui.Screen
import com.mirage.utils.PLATFORM
import com.mirage.utils.TestSamples
import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.game.states.GameStateSnapshot
import com.mirage.utils.game.states.SimplifiedState
import com.mirage.utils.game.states.SnapshotManager
import com.mirage.utils.game.states.StateDifference
import com.mirage.utils.messaging.*
import com.mirage.utils.virtualscreen.VirtualScreen
import rx.Observable

class GameScreen(gameMap: GameMap, virtualScreen: VirtualScreen) : Screen {

    private val uiState : GameUIState = GameUIState(virtualScreen)

    override val inputProcessor : GameInputProcessor = when (PLATFORM) {
        "desktop", "test" -> DesktopGameInputProcessor(uiState)
        else -> DesktopGameInputProcessor(uiState)
    }

    private val uiRenderer : GameUIRenderer = when (PLATFORM) {
        "desktop", "test" -> DesktopGameUIRenderer()
        else -> DesktopGameUIRenderer()
    }

    private val gameView = GameViewImpl(gameMap)

    private val snapshotManager = SnapshotManager()

    //TODO какие-то костыли, подумать как это всё перенести в модуль client
    private var playerID : Long? = null
    private var lastReceivedState : SimplifiedState = TestSamples.TEST_NO_GAME_OBJECTS

    override fun handleServerMessage(msg: ServerMessage) {
        when (msg) {
            is InitialGameStateMessage -> {
                gameView.loadDrawers(msg.initialState)
                println("Loading drawers for initial state: ${msg.initialState}")
                snapshotManager.addNewSnapshot(GameStateSnapshot(msg.initialState, StateDifference(), msg.stateCreatedTimeMillis))
                playerID = msg.playerID
                lastReceivedState = msg.initialState
            }
            is GameStateUpdateMessage -> {
                gameView.updateDrawers(lastReceivedState, msg.diff)
                lastReceivedState = msg.diff.projectOn(lastReceivedState)
                snapshotManager.addNewSnapshot(GameStateSnapshot(lastReceivedState, msg.diff, msg.stateCreatedTimeMillis))
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
        gameView.renderGameState(virtualScreen, state, state.entities[playerID]?.position ?: Point(0f, 0f))
        uiRenderer.renderUI(virtualScreen, uiState, currentTimeMillis)
    }

    override val inputMessages: Observable<ClientMessage> = inputProcessor.inputMessages

    override fun resize(virtualWidth: Float, virtualHeight: Float) = uiState.resize(virtualWidth, virtualHeight)

}