package com.mirage.utils

import com.badlogic.gdx.maps.MapObject
import com.mirage.utils.datastructures.Point

/**
 * Сообщение об изменении состояния карты
 * Сохраняется в MessageQueue и передаётся клиентам
 */
sealed class UpdateMessage

/**
 * Клиент должен запустить данный скрипт
 */
data class RunScriptMessage(val scriptName: String) : UpdateMessage()

/**
 * Добавление нового объекта на карту
 */
data class NewObjectMessage(val id: Long, val obj: MapObject) : UpdateMessage()

/**
 * Удаление объекта с карты
 */
data class RemoveObjectMessage(val id: Long) : UpdateMessage()

/**
 * Перемещение объекта
 */
data class MoveObjectMessage(val id: Long, val newPosition: Point) : UpdateMessage()

/**
 * Изменение направления движения объекта
 */
data class MoveDirectionChangeMessage(val id: Long, val moveDirection: MoveDirection) : UpdateMessage()

/**
 * Изменение карты (новая карта полностью чистая, после этого сообщения должны идти сообщения добавления объектов)
 */
data class MapChangeMessage(val mapName: String) : UpdateMessage()