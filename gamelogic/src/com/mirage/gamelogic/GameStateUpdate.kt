package com.mirage.gamelogic

import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.game.objects.MutableGameObjects
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.ServerMessage
import java.util.*


/**
 * Функция со всей игровой логикой.
 * Обрабатывает изменения состояния игры за заданное время.
 * Возвращает новое ИЗМЕНЯЕМОЕ состояние игры и очередь сообщений [ServerMessage], которые должны быть отправлены ВСЕМ клиентам.
 * Эта коллекция НЕ содержит сообщения об изменении состояния игры.
 * @param delta Интервал в мс, изменение состояния за который обрабатывается.
 * @param originState Изначальное состояние игры.
 * @param gameMap Карта игры.
 * @param clientMessages Коллекция пар "id персонажа игрока - сообщение, полученное от этого игрока за время тика",
 *                          итерируемая в порядке получения сообщений.
 */
internal fun updateState(
        delta: Long,
        originState: GameObjects,
        gameMap: GameMap,
        clientMessages: Iterable<Pair<Long, ClientMessage>>
) : Pair<MutableGameObjects, ArrayDeque<ServerMessage>> {
    val objs = originState.createMutableObjectsCopy()
    val serverMessages : ArrayDeque<ServerMessage> = ArrayDeque()
    //TODO Обработка сообщений от клиентов
    for ((id, msg) in clientMessages) {
        println("Got client message $id $msg")

    }
    println("Delta time $delta")
    //TODO Обработка логики

    return Pair(objs, serverMessages)
}