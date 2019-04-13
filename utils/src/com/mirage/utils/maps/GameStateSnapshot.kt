package com.mirage.utils.maps

import com.mirage.utils.gameobjects.GameObjects

/**
 * Полная информация о текущем состоянии игры.
 * Создаётся логикой после каждого тика цикла логики.
 * Содержит полную информацию о состоянии на момент начала тика,
 * изменение состояния за время тика и список скриптов, которые должны выполнить клиенты,
 * которые были подключены на момент тика.
 */
data class GameStateSnapshot(val originState: GameObjects,
                             val stateDifference: StateDifference)