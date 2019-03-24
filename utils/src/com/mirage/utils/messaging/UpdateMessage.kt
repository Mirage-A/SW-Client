package com.mirage.utils.messaging

import com.badlogic.gdx.maps.MapObject
import com.mirage.utils.INNER_DLMTR

/**
 * Сообщение об изменении состояния карты
 * Сохраняется в MessageQueue и передаётся клиентам
 */
sealed class UpdateMessage {
    companion object {
        /**
         * Десериализует сообщение из строки.
         * @throws Exception если строка некорректна и была получена не методом serialize
         * @see serialize
         */
        fun deserialize(str: String) : UpdateMessage {
            val args = str.split(INNER_DLMTR)
            return when (args[0]) {
                "SCR" -> RunScriptMessage(args[1])
                "NEW" -> NewObjectMessage(args[1].toLong(), deserializeMapObject(args[2]))
                "RM" -> RemoveObjectMessage(args[1].toLong())
                "PS" -> PositionSnapshotMessage(deserializePositionSnapshot(args[1]))
                "MC" -> MapChangeMessage(args[1])
                "EOP" -> EndOfPackageMessage(args[1].toLong())
                else -> throw Exception("Incorrect message: $args")
            }
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
    override fun serialize(): String = "NEW$INNER_DLMTR$id$INNER_DLMTR${obj.serialize()}"//TODO
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