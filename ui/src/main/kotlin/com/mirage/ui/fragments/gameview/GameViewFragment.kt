package com.mirage.ui.fragments.gameview

import com.mirage.core.datastructures.Point
import com.mirage.core.extensions.EntityID
import com.mirage.core.extensions.GameMapName
import com.mirage.core.extensions.TimeMillis
import com.mirage.core.game.maps.GameMap
import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.states.GameStateSnapshot
import com.mirage.core.game.states.SimplifiedState
import com.mirage.core.game.states.SnapshotManager
import com.mirage.core.game.states.StateDifference
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.ui.screens.game.GameState
import com.mirage.ui.widgets.Widget
import com.mirage.view.GameView
import com.mirage.view.GameViewImpl
import java.util.*

/** Fragment wrapper for GameView class, rendering game state on virtual screen */
internal class GameViewFragment(
        /** State of GameScreen (using this fragment only makes sense in game screen) */
        private val gameState: GameState,
        /** This method is invoked when player changes target by clicking on entity on pressing ESC */
        var targetListener: ((EntityID?) -> Unit)? = null
) : Widget {

    override var isVisible: Boolean = true

    private val gameView: GameView = GameViewImpl(gameState.gameMapName, gameState.gameMap)

    private val equipmentChangeRequests: MutableList<Pair<EntityID, Equipment>> = LinkedList()

    private val snapshotManager: SnapshotManager = SnapshotManager { oldState, difference ->
        /** Updating drawers for new snapshot */
        gameView.updateDrawers(oldState, difference)
        /** Processing equipment change requests */
        val iterator = equipmentChangeRequests.listIterator()
        while (iterator.hasNext()) {
            val (id, equipment) = iterator.next()
            val oldEntity = oldState.entities[id]
            val newEntity = difference.entitiesDifference.new[id]
            val changedEntity = difference.entitiesDifference.changed[id]
            val entity = when {
                changedEntity != null -> changedEntity
                newEntity != null -> newEntity
                difference.entitiesDifference.removed.contains(id) -> null
                else -> oldEntity
            }
            if (entity != null) {
                gameView.setHumanoidEquipment(id, entity, equipment)
                iterator.remove()
            }
        }
    }

    private var lastRenderedState: SimplifiedState = SimplifiedState()

    fun setInitialState(initialState: SimplifiedState, createdTimeMillis: TimeMillis) {
        snapshotManager.setInitialState(initialState, createdTimeMillis)
    }

    fun addSnapshot(difference: StateDifference, createdTimeMillis: TimeMillis) {
        snapshotManager.addSnapshot(difference, createdTimeMillis)
    }

    fun setEquipment(humanoidID: EntityID, equipment: Equipment) {
        equipmentChangeRequests.add(humanoidID to equipment)
    }

    override fun draw(virtualScreen: VirtualScreen) {
        val state = snapshotManager.getInterpolatedSnapshot(System.currentTimeMillis())
        gameView.renderGameState(
                virtualScreen = virtualScreen,
                state = state,
                playerPositionOnScene = state.entities[gameState.playerID]?.position ?: Point(0f, 0f),
                targetID = gameState.targetID,
                isTargetEnemy = state.entities[gameState.playerID]?.factionID != state.entities[gameState.targetID]?.factionID
        )
        lastRenderedState = state
    }

    override fun resize(virtualWidth: Float, virtualHeight: Float) {}
}