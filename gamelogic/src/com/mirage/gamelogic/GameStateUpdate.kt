package com.mirage.gamelogic

import com.mirage.utils.maps.GameMap
import com.mirage.utils.maps.GameObjects
import com.mirage.utils.maps.StateDifference
import com.mirage.utils.messaging.ClientMessage


/**
 * Функция со всей игровой логикой.
 * Обрабатывает изменения состояния игры за заданное время и возвращает изменения, которые должны быть применены.
 */
internal fun updateState(delta: Long,
                        originState: GameObjects,
                        gameMap: GameMap,
                        clientMessages: Iterable<ClientMessage>) : StateDifference {
    val objs = originState.createMutableObjectsCopy()
    val newClientScripts : MutableList<String> = ArrayList()
    //TODO Обработка сообщений от клиентов
    for (msg in clientMessages) {
        println(msg)
    }
    println(delta)
    //TODO Обработка логики

    return objs.findDifferenceWithOrigin(newClientScripts)
}