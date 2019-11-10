package com.mirage.ui.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.ui.Screen
import com.mirage.utils.TestSamples
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.treeSetOf
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.game.states.GameStateSnapshot
import com.mirage.utils.game.states.SnapshotManager
import com.mirage.utils.game.states.StateDifference
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.GameStateUpdateMessage
import com.mirage.utils.messaging.InitialGameStateMessage
import com.mirage.utils.messaging.ServerMessage
import com.mirage.view.GameViewImpl
import rx.Observable

class GameScreen(gameMap: GameMap) : Screen {

    private val inputProcessor = GameInputProcessor()

    private val uiRenderer = GameUIRenderer()

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
        inputProcessor.uiState.lock.unlock()
        val state = snapshotManager.getInterpolatedSnapshot(currentTimeMillis)
        gameView.renderGameState(batch, state, state.objects[playerID]?.position ?: Point(0f, 0f), screenWidth, screenHeight)
        uiRenderer.renderUI(batch, screenWidth, screenHeight, uiStateSnapshot, currentTimeMillis)
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