package com.mirage.ui.fragments.gameview

import com.badlogic.gdx.Input
import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.game.objects.SimplifiedState
import com.mirage.core.game.objects.StateDifference
import com.mirage.core.messaging.InteractionClientMessage
import com.mirage.core.messaging.SetTargetClientMessage
import com.mirage.core.utils.*
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.ui.screens.ClientMessageListener
import com.mirage.ui.screens.game.GameState
import com.mirage.ui.widgets.Widget
import com.mirage.view.GameView
import com.mirage.view.GameViewImpl
import com.mirage.view.utils.getVirtualScreenPointFromScene
import java.util.*

/** Fragment wrapper for GameView class, rendering game state on virtual screen */
internal class GameViewFragment(
        /** State of GameScreen (using this fragment only makes sense in game screen) */
        private val gameState: GameState,
        var listener: ClientMessageListener
) : Widget {

    override var isVisible: Boolean = true

    private val gameView: GameView = GameViewImpl(gameState.gameMapName, gameState.gameMap)

    private val equipmentChangeRequests: MutableList<Pair<EntityID, Equipment>> = LinkedList()

    private val snapshotManager: SnapshotManager = SnapshotManager { oldState, difference ->
        /** Updating drawers for new snapshot */
        /** Updating drawers for new snapshot */
        gameView.updateDrawers(oldState, difference)
        /** Processing equipment change requests */
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

    var lastRenderedState: SimplifiedState = SimplifiedState()
        private set

    /** Move buttons release time*/
    private var wReleasedTime = 0L
    private var aReleasedTime = 0L
    private var sReleasedTime = 0L
    private var dReleasedTime = 0L

    /** If two move buttons are released in time lesser than this interval, character will stay in diagonal move direction */
    private val diagonalMoveDirectionInterval: IntervalMillis = 50L

    fun setInitialState(initialState: SimplifiedState, createdTimeMillis: TimeMillis) {
        gameView.loadDrawers(initialState)
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

    override fun touchUp(virtualPoint: Point): Boolean = false

    override fun touchDown(virtualPoint: Point): Boolean {
        val player = lastRenderedState.entities[gameState.playerID] ?: return true
        val playerOnVirtualScreen = getVirtualScreenPointFromScene(player.position)
        val virtualHitPoint = Point(
                x = virtualPoint.x + playerOnVirtualScreen.x,
                y = virtualPoint.y + playerOnVirtualScreen.y + DELTA_CENTER_Y
        )
        val targetID = gameView.hit(virtualHitPoint, lastRenderedState)
        if (targetID != null && targetID != gameState.targetID) {
            gameState.targetID = targetID
            listener(SetTargetClientMessage(targetID))
        }
        return true
    }

    override fun mouseMoved(virtualPoint: Point): Boolean = false

    override fun keyTyped(character: Char): Boolean = false

    override fun scrolled(amount: Int): Boolean = false

    override fun touchDragged(virtualPoint: Point): Boolean = false

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.W -> {
                wReleasedTime = System.currentTimeMillis()
                if (gameState.bufferedMoving == true) {
                    when (gameState.bufferedMoveDirection) {
                        MoveDirection.UP_LEFT -> {
                            startMoving(MoveDirection.LEFT)
                        }
                        MoveDirection.UP_RIGHT -> {
                            startMoving(MoveDirection.RIGHT)
                        }
                        MoveDirection.UP -> {
                            if (aReleasedTime >= dReleasedTime && wReleasedTime - aReleasedTime < diagonalMoveDirectionInterval) {
                                setMoveDirection(MoveDirection.UP_LEFT)
                            } else if (dReleasedTime >= aReleasedTime && wReleasedTime - dReleasedTime < diagonalMoveDirectionInterval) {
                                setMoveDirection(MoveDirection.UP_RIGHT)
                            }
                            stopMoving()
                        }
                        else -> {
                        }
                    }
                }
            }
            Input.Keys.A -> {
                aReleasedTime = System.currentTimeMillis()
                if (gameState.bufferedMoving == true) {
                    when (gameState.bufferedMoveDirection) {
                        MoveDirection.UP_LEFT -> {
                            startMoving(MoveDirection.UP)
                        }
                        MoveDirection.DOWN_LEFT -> {
                            startMoving(MoveDirection.DOWN)
                        }
                        MoveDirection.LEFT -> {
                            if (wReleasedTime >= sReleasedTime && aReleasedTime - wReleasedTime < diagonalMoveDirectionInterval) {
                                setMoveDirection(MoveDirection.UP_LEFT)
                            } else if (sReleasedTime >= wReleasedTime && aReleasedTime - sReleasedTime < diagonalMoveDirectionInterval) {
                                setMoveDirection(MoveDirection.DOWN_LEFT)
                            }
                            stopMoving()
                        }
                        else -> {
                        }
                    }
                }
            }
            Input.Keys.S -> {
                sReleasedTime = System.currentTimeMillis()
                if (gameState.bufferedMoving == true) {
                    when (gameState.bufferedMoveDirection) {
                        MoveDirection.DOWN_LEFT -> {
                            startMoving(MoveDirection.LEFT)
                        }
                        MoveDirection.DOWN_RIGHT -> {
                            startMoving(MoveDirection.RIGHT)
                        }
                        MoveDirection.DOWN -> {
                            if (aReleasedTime >= dReleasedTime && sReleasedTime - aReleasedTime < diagonalMoveDirectionInterval) {
                                setMoveDirection(MoveDirection.DOWN_LEFT)
                            } else if (dReleasedTime >= aReleasedTime && sReleasedTime - dReleasedTime < diagonalMoveDirectionInterval) {
                                setMoveDirection(MoveDirection.DOWN_RIGHT)
                            }
                            stopMoving()
                        }
                        else -> {
                        }
                    }
                }
            }
            Input.Keys.D -> {
                dReleasedTime = System.currentTimeMillis()
                if (gameState.bufferedMoving == true) {
                    when (gameState.bufferedMoveDirection) {
                        MoveDirection.UP_RIGHT -> {
                            startMoving(MoveDirection.UP)
                        }
                        MoveDirection.DOWN_RIGHT -> {
                            startMoving(MoveDirection.DOWN)
                        }
                        MoveDirection.RIGHT -> {
                            if (wReleasedTime >= sReleasedTime && dReleasedTime - wReleasedTime < diagonalMoveDirectionInterval) {
                                setMoveDirection(MoveDirection.UP_RIGHT)
                            } else if (sReleasedTime >= wReleasedTime && dReleasedTime - sReleasedTime < diagonalMoveDirectionInterval) {
                                setMoveDirection(MoveDirection.DOWN_RIGHT)
                            }
                            stopMoving()
                        }
                        else -> {
                        }
                    }
                }
            }

        }
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.W -> {
                if (gameState.bufferedMoving == true) {
                    when (gameState.bufferedMoveDirection) {
                        MoveDirection.LEFT -> {
                            startMoving(MoveDirection.UP_LEFT)
                        }
                        MoveDirection.RIGHT -> {
                            startMoving(MoveDirection.UP_RIGHT)
                        }
                        else -> {
                            startMoving(MoveDirection.UP)
                        }
                    }
                } else {
                    startMoving(MoveDirection.UP)
                }
            }
            Input.Keys.A -> {
                if (gameState.bufferedMoving == true) {
                    when (gameState.bufferedMoveDirection) {
                        MoveDirection.UP -> {
                            startMoving(MoveDirection.UP_LEFT)
                        }
                        MoveDirection.DOWN -> {
                            startMoving(MoveDirection.DOWN_LEFT)
                        }
                        else -> {
                            startMoving(MoveDirection.LEFT)
                        }
                    }
                } else {
                    startMoving(MoveDirection.LEFT)
                }
            }
            Input.Keys.S -> {
                if (gameState.bufferedMoving == true) {
                    when (gameState.bufferedMoveDirection) {
                        MoveDirection.LEFT -> {
                            startMoving(MoveDirection.DOWN_LEFT)
                        }
                        MoveDirection.RIGHT -> {
                            startMoving(MoveDirection.DOWN_RIGHT)
                        }
                        else -> {
                            startMoving(MoveDirection.DOWN)
                        }
                    }
                } else {
                    startMoving(MoveDirection.DOWN)
                }
            }
            Input.Keys.D -> {
                if (gameState.bufferedMoving == true) {
                    when (gameState.bufferedMoveDirection) {
                        MoveDirection.UP -> {
                            startMoving(MoveDirection.UP_RIGHT)
                        }
                        MoveDirection.DOWN -> {
                            startMoving(MoveDirection.DOWN_RIGHT)
                        }
                        else -> {
                            startMoving(MoveDirection.RIGHT)
                        }
                    }
                } else {
                    startMoving(MoveDirection.RIGHT)
                }
            }
            Input.Keys.ESCAPE -> {
                if (gameState.targetID != null) {
                    gameState.targetID = null
                    listener(SetTargetClientMessage(null))
                }
            }
            Input.Keys.E -> {
                val targetID = gameState.targetID
                val player = lastRenderedState.entities[gameState.playerID]
                val target = lastRenderedState.entities[targetID]
                if (targetID != null && player != null && target != null &&
                        player.position..target.position < target.interactionRange) {
                    listener(InteractionClientMessage(targetID))
                }
            }
        }
        return true
    }


    private fun startMoving(md: MoveDirection) {
        gameState.bufferedMoveDirection = md
        gameState.bufferedMoving = true
    }

    private fun stopMoving() {
        gameState.bufferedMoving = false
    }

    private fun setMoveDirection(md: MoveDirection) {
        gameState.bufferedMoveDirection = md
    }

}