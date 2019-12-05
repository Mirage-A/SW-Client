package com.mirage.utils.game.states


/**
 * Snapshot of a game state, which is created on each tick of game logic.
 * Contains information about full game state after update, difference between final state
 * and previous state and time of final state creation.
 */
data class GameStateSnapshot(val finalState: SimplifiedState,
                             val stateDifference: StateDifference,
                             val createdTimeMillis: Long) : Comparable<GameStateSnapshot> {

    override fun compareTo(other: GameStateSnapshot): Int = createdTimeMillis.compareTo(other.createdTimeMillis)

}