package com.mirage.gameview

import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.states.SimplifiedState
import com.mirage.utils.game.states.StateDifference
import com.mirage.utils.virtualscreen.VirtualScreen

/** Visual representation of the state of the game */
interface GameView {

    /**
     * Loads all resources needed to render state [initialState].
     * This method is recommended to be invoked on receiving initial state from logic.
     */
    fun loadDrawers(initialState: SimplifiedState)

    /**
     * Updates drawers to render next state.
     * This method should be called on moving to next [com.mirage.utils.game.states.GameStateSnapshot],
     * or on receiving new [com.mirage.utils.messaging.GameStateUpdateMessage].
     */
    fun updateDrawers(oldState: SimplifiedState, diff: StateDifference)

    /** Renders game state [state] on [virtualScreen] */
    fun renderGameState(virtualScreen: VirtualScreen, state: SimplifiedState, playerPositionOnScene: Point, targetID: Long?, isTargetEnemy: Boolean)

    /** Finds an entity which is under a click on a virtual screen on coordinates [virtualPoint] */
    fun hit(virtualPoint: Point, lastRenderedState: SimplifiedState): Long?


}