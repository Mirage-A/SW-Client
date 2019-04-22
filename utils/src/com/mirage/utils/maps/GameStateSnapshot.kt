package com.mirage.utils.maps

import com.mirage.utils.gameobjects.GameObjects

/**
 * Создаётся логикой после каждого тика цикла логики.
 * Содержит полную информацию о состоянии на момент конца тика,
 * изменение состояния за время тика и список скриптов, которые должны выполнить клиенты,
 * которые были подключены на момент тика.
 */
data class GameStateSnapshot(val finalState: GameObjects,
                             val stateDifference: StateDifference,
                             val createdTimeMillis: Long) : Comparable<GameStateSnapshot> {

    override fun compareTo(other: GameStateSnapshot): Int = createdTimeMillis.compareTo(other.createdTimeMillis)

}