package com.mirage.view

import com.mirage.core.utils.Point
import com.mirage.core.utils.EntityID
import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.objects.SimplifiedEntity
import com.mirage.core.game.objects.SimplifiedState
import com.mirage.core.game.objects.StateDifference
import com.mirage.core.virtualscreen.VirtualScreen

/** Visual representation of the state of the game */
interface GameView {

    /**
     * Loads all resources needed to render state [initialState].
     * This method is recommended to be invoked on receiving initial state from logic.
     */
    fun loadDrawers(initialState: SimplifiedState)

    /**
     * Updates drawers to render next state.
     * This method should be called on moving to next snapshot,
     * or on receiving new [com.mirage.core.messaging.GameStateUpdateMessage].
     */
    fun updateDrawers(oldState: SimplifiedState, diff: StateDifference)

    /** Sets drawer of entity [id] to instance of [HumanoidDrawerTemplate] with equipment [newEquipment] */
    fun setHumanoidEquipment(id: EntityID, entity: SimplifiedEntity, newEquipment: Equipment)

    /** Renders game state [state] on [virtualScreen] */
    fun renderGameState(virtualScreen: VirtualScreen, state: SimplifiedState, playerPositionOnScene: Point, targetID: Long?, isTargetEnemy: Boolean)

    /** Finds an entity which is under a click on a virtual screen on coordinates [virtualPoint] */
    fun hit(virtualPoint: Point, lastRenderedState: SimplifiedState): Long?


}