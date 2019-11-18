package com.mirage.ui.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
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
import com.mirage.gameview.GameViewImpl
import rx.Observable

class GameScreen(gameMap: GameMap) : Screen {

    private val inputProcessor : GameInputProcessor = when (PLATFORM) {
        "desktop", "test" -> DesktopGameInputProcessor()
        else -> DesktopGameInputProcessor()
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

    override fun render(batch: SpriteBatch, screenWidth: Int, screenHeight: Int, currentTimeMillis: Long) {
        inputProcessor.uiState.lock.lock()
        val uiStateSnapshot = inputProcessor.uiState.copy()
        uiStateSnapshot.let {
            if (it.bufferedMoving != it.lastSentMoving) {
                it.bufferedMoving?.let { newMoving ->
                    inputProcessor.uiState.lastSentMoving = newMoving
                    inputProcessor.inputMessages.onNext(SetMovingClientMessage(newMoving))
                }
            }
            if (it.bufferedMoveDirection != it.lastSentMoveDirection) {
                it.bufferedMoveDirection?.let { newMoveDirection ->
                    inputProcessor.uiState.lastSentMoveDirection = newMoveDirection
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
        inputProcessor.uiState.lock.unlock()
        val state = snapshotManager.getInterpolatedSnapshot(currentTimeMillis)
        batch.begin()
        gameView.renderGameState(batch, state, state.objects[playerID]?.position ?: Point(0f, 0f), screenWidth, screenHeight)
        uiRenderer.renderUI(batch, screenWidth, screenHeight, uiStateSnapshot, currentTimeMillis)
        batch.end()
    }

    override val inputMessages: Observable<ClientMessage> = inputProcessor.inputMessages

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean =
            inputProcessor.touchUp(screenX, screenY, pointer, button)

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean =
            inputProcessor.mouseMoved(screenX, screenY)

    override fun keyTyped(character: Char): Boolean =
            inputProcessor.keyTyped(character)

    override fun scrolled(amount: Int): Boolean =
            inputProcessor.scrolled(amount)

    override fun keyUp(keycode: Int): Boolean =
            inputProcessor.keyUp(keycode)

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean =
            inputProcessor.touchDragged(screenX, screenY, pointer)

    override fun keyDown(keycode: Int): Boolean =
            inputProcessor.keyDown(keycode)

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean =
            inputProcessor.touchDown(screenX, screenY, pointer, button)
}