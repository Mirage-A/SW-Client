package com.mirage.ui.screens.game

import com.mirage.view.GameViewImpl
import com.mirage.view.utils.getVirtualScreenPointFromScene
import com.mirage.ui.screens.Screen
import com.mirage.core.DELTA_CENTER_Y
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
import com.mirage.ui.screens.AbstractScreen
import com.mirage.ui.screens.ClientMessageListener
import com.mirage.ui.widgets.Widget
import rx.Observable
import kotlin.math.min

class GameScreen(
        virtualScreen: VirtualScreen,
        gameMapName: GameMapName,
        private val listener: ClientMessageListener
) : AbstractScreen(virtualScreen) {

    //private val gameView = GameViewImpl(gameMapName, gameMap)

    private val gameState = GameState(gameMapName)
    private val gameWidgets = GameWidgets(virtualScreen, gameState).apply {
        initializeSizeUpdaters()
        initializeListeners(gameState, listener)
    }

    override val rootWidget: Widget = gameWidgets.rootWidget

    override fun handleServerMessage(msg: ServerMessage) {
        when (msg) {
            is InitialGameStateMessage -> {
                gameWidgets.gameView.setInitialState(msg.initialState, msg.stateCreatedTimeMillis)
                gameState.playerID = msg.playerID
                gameState.lastReceivedState = msg.initialState
            }
            is GameStateUpdateMessage -> {
                gameWidgets.gameView.addSnapshot(msg.diff, msg.stateCreatedTimeMillis)
            }
            is LocalQuestUpdateMessage -> {
                //TODO Show notification about quest progress
                gameState.localQuestProgress[msg.localQuestName] = msg.newPhaseID
                gameWidgets.questWindow.updateQuestWindow()
            }
            is GlobalQuestUpdateMessage -> {
                //TODO Show notification about quest progress
                Prefs.profile.globalQuestProgress[msg.globalQuestName] = msg.newPhaseID
                gameWidgets.questWindow.updateQuestWindow()
            }
            is HumanoidEquipmentUpdateMessage -> {
                gameWidgets.gameView.setEquipment(msg.objectID, msg.newEquipment)
            }
            is GameOverMessage -> {
                listener(CloseConnectionMessage())
                gameState.gameOver = true
                gameState.gameOverStartTime = System.currentTimeMillis()
                gameWidgets.gameOverMessage.boundedLabel?.text = msg.message ?: "You died"
                gameWidgets.gameOverMessage.isVisible = msg.message != null
                gameWidgets.gameCompositeWidget.isVisible = false
                gameWidgets.gameOverBackground.isVisible = true
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
        if (uiState.gameOver) {
            val timePassed = currentTimeMillis - uiState.gameOverStartTime
            val alpha = min(1f, timePassed.toFloat() / screenFadingInterval.toFloat()) * maxFadingAlpha
            if (timePassed > screenFadingInterval) {
                uiState.gameOverCompositeWidget.isVisible = true
            }
        }
        if (!uiState.gameOver) {
            val player = uiState.lastRenderedState.entities[uiState.playerID]
            uiState.playerHealthPane.currentResource = player?.health ?: 0
            uiState.playerHealthPane.maxResource = player?.maxHealth ?: 0
            val target = uiState.lastRenderedState.entities[uiState.targetID]
            uiState.targetHealthPane.isVisible = target != null
            uiState.targetNameArea.isVisible = target != null
            if (target != null) {
                uiState.targetHealthPane.currentResource = target.health
                uiState.targetHealthPane.maxResource = target.maxHealth
                uiState.targetNameLabel.text = target.name
            }
        }
        for (i in uiState.widgets.size - 1 downTo 0) {
            uiState.widgets[i].draw(virtualScreen)
        }
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
        gameState.targetID = null
    }


    init {
        resize(virtualScreen.width, virtualScreen.height)
    }

}