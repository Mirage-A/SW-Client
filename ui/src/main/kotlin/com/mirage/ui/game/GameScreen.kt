package com.mirage.ui.game

import com.mirage.gameview.GameViewImpl
import com.mirage.ui.Screen
import com.mirage.utils.PLATFORM
import com.mirage.utils.TestSamples
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.treeSetOf
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.game.states.GameStateSnapshot
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
    private var lastReceivedState : GameObjects = TestSamples.TEST_NO_GAME_OBJECTS

    override fun handleServerMessage(msg: ServerMessage) {
        when (msg) {
            is InitialGameStateMessage -> {
                snapshotManager.addNewSnapshot(GameStateSnapshot(msg.initialObjects, StateDifference(hashMapOf(), treeSetOf(), hashMapOf()), msg.stateCreatedTimeMillis))
                playerID = msg.playerID
                lastReceivedState = msg.initialObjects
                gameView.loadDrawers(msg.initialObjects)
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
                        GameObject.MoveDirection.RIGHT -> GameObject.MoveDirection.UP_RIGHT
                        GameObject.MoveDirection.UP_RIGHT -> GameObject.MoveDirection.UP
                        GameObject.MoveDirection.UP -> GameObject.MoveDirection.UP_LEFT
                        GameObject.MoveDirection.UP_LEFT -> GameObject.MoveDirection.LEFT
                        GameObject.MoveDirection.LEFT -> GameObject.MoveDirection.DOWN_LEFT
                        GameObject.MoveDirection.DOWN_LEFT -> GameObject.MoveDirection.DOWN
                        GameObject.MoveDirection.DOWN -> GameObject.MoveDirection.DOWN_RIGHT
                        GameObject.MoveDirection.DOWN_RIGHT -> GameObject.MoveDirection.RIGHT
                    }))
                }
            }
        }
        val state = snapshotManager.getInterpolatedSnapshot(currentTimeMillis)
        gameView.renderGameState(virtualScreen, state, state.objects[playerID]?.position ?: Point(0f, 0f))
        uiRenderer.renderUI(virtualScreen, uiState, currentTimeMillis)
    }

    override val inputMessages: Observable<ClientMessage> = inputProcessor.inputMessages

    override fun resize(virtualWidth: Float, virtualHeight: Float) = uiState.resize(virtualWidth, virtualHeight)

}