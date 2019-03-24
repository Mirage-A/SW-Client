package com.mirage.utils.messaging

import com.badlogic.gdx.maps.MapObject
import com.mirage.utils.INNER_DLMTR

/**
 * Сообщение об изменении состояния карты
 * Сохраняется в MessageQueue и передаётся клиентам
 */
sealed class UpdateMessage {
    companion object {
        fun deserialize(str: String) : UpdateMessage {//TODO
            return EndOfPackageMessage(System.currentTimeMillis())
        }
    }
    abstract fun serialize() : String
}

/**
 * Клиент должен запустить данный скрипт
 */
data class RunScriptMessage(val scriptName: String) : UpdateMessage() {
    override fun serialize(): String = "SCR$INNER_DLMTR$scriptName"
}

/**
 * Добавление нового объекта на карту
 */
data class NewObjectMessage(val id: Long, val obj: MapObject) : UpdateMessage() {
    override fun serialize(): String = "MV$INNER_DLMTR$id$INNER_DLMTR${obj.serialize()}"//TODO
}

/**
 * Удаление объекта с карты
 */
data class RemoveObjectMessage(val id: Long) : UpdateMessage() {
    override fun serialize(): String = "RM$INNER_DLMTR$id"
}

/**
 * Новые позиции объектов
 */
data class PositionSnapshotMessage(val snapshot: PositionSnapshot) : UpdateMessage() {
    override fun serialize(): String = "PS$INNER_DLMTR${snapshot.serialize()}"//TODO
}

/**
 * Изменение карты (новая карта полностью чистая, после этого сообщения должны идти сообщения добавления объектов)
 */
data class MapChangeMessage(val mapName: String) : UpdateMessage() {
    override fun serialize(): String = "MC$INNER_DLMTR$mapName"
}

/**
 * Сообщение о конце пакета (т.е. о конце итерации цикла)
 */
data class EndOfPackageMessage(val sendTimeMillis: Long) : UpdateMessage() {
    override fun serialize(): String = "EOP$INNER_DLMTR$sendTimeMillis"
}