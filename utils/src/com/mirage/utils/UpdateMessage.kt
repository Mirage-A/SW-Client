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
 * Новые позиции объектов
 */
data class PositionSnapshotMessage(val snapshot: PositionSnapshot) : UpdateMessage()

/**
 * Изменение карты (новая карта полностью чистая, после этого сообщения должны идти сообщения добавления объектов)
 */
data class MapChangeMessage(val mapName: String) : UpdateMessage()

/**
 * Сообщение о конце пакета (т.е. о конце итерации цикла)
 */
data class EndOfPackageMessage(val sendTimeMillis: Long) : UpdateMessage()