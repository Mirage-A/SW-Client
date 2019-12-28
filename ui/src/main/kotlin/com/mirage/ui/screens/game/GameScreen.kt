package com.mirage.ui.screens.game

import com.mirage.core.utils.GameMapName
import com.mirage.core.messaging.*
import com.mirage.core.preferences.Prefs
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.ui.screens.AbstractScreen
import com.mirage.ui.screens.ClientMessageListener
import com.mirage.ui.widgets.Widget

class GameScreen(
        virtualScreen: VirtualScreen,
        gameMapName: GameMapName,
        private val listener: ClientMessageListener
) : AbstractScreen(virtualScreen) {

    private val gameState = GameState(gameMapName)
    private val gameWidgets = GameWidgets(virtualScreen, gameState, listener).apply {
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
                gameWidgets.gameOverBackground.isVisible = true
            }
        }
    }

    override fun render(virtualScreen: VirtualScreen) {
        with(gameState) {
            if (bufferedMoving != lastSentMoving) {
                bufferedMoving?.let { newMoving ->
                    lastSentMoving = newMoving
                    listener(SetMovingClientMessage(newMoving))
                }
            }
            if (bufferedMoveDirection != lastSentMoveDirection) {
                bufferedMoveDirection?.let { newMoveDirection ->
                    lastSentMoveDirection = newMoveDirection
                    listener(MoveDirectionClientMessage(newMoveDirection.fromViewToScene()))
                }
            }
            if (gameOver && System.currentTimeMillis() > gameOverStartTime + screenFadingInterval) {
                gameWidgets.gameOverCompositeWidget.isVisible = true
            }
        }
        with(gameWidgets) {
            val player = gameView.lastRenderedState.entities[gameState.playerID]
            playerHealthPane.currentResource = player?.health ?: 0
            playerHealthPane.maxResource = player?.maxHealth ?: 0
            val target = gameView.lastRenderedState.entities[gameState.targetID]
            targetHealthPane.isVisible = target != null
            targetNameArea.isVisible = target != null
            if (target != null) {
                targetHealthPane.currentResource = target.health
                targetHealthPane.maxResource = target.maxHealth
                targetNameArea.boundedLabel?.text = target.name
            }
        }
        rootWidget.draw(virtualScreen)
    }

    init {
        virtualScreen.setTileSet(gameState.gameMap.tileSetName)
        resize(virtualScreen.width, virtualScreen.height)
    }

}