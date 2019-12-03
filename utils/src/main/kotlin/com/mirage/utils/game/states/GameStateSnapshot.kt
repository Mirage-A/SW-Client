package com.mirage.utils.game.states


/**
 * Создаётся логикой после каждого тика цикла логики.
 * Содержит полную информацию о состоянии на момент конца тика и об
 * изменении состояния за время тика.
 */
data class GameStateSnapshot(val finalState: SimplifiedState,
                             val stateDifference: StateDifference,
                             val createdTimeMillis: Long) : Comparable<GameStateSnapshot> {

    override fun compareTo(other: GameStateSnapshot): Int = createdTimeMillis.compareTo(other.createdTimeMillis)

}