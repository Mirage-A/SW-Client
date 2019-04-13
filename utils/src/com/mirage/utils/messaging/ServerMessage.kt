package com.mirage.utils.messaging

import com.badlogic.gdx.maps.MapObject
import com.mirage.utils.INNER_DLMTR
import com.mirage.utils.gameobjects.GameObjects
import com.mirage.utils.maps.StateDifference

/**
 * Сообщение об изменении состояния карты
 * Сохраняется в MessageQueue и передаётся клиентам
 * //TODO Забить на всякую преждевременную оптимизацию и заюзать GSON
 */
sealed class ServerMessage {
    companion object {
        /**
         * Десериализует сообщение из строки.
         * @throws Exception если строка некорректна и была получена не методом serialize
         * @see serialize
         */
        fun deserialize(str: String) : ServerMessage {
            //TODO Поддержка новых типов сообщений
            val args = str.split(INNER_DLMTR)
            return when (args[0]) {
                "SCR" -> RunScriptMessage(args[1])
                "NEW" -> NewObjectMessage(args[1].toLong(), deserializeMapObject(args[2]))
                "RM" -> RemoveObjectMessage(args[1].toLong())
                "PS" -> PositionSnapshotMessage(deserializePositionSnapshot(args[1]))
                "MC" -> MapChangeMessage(args[1])
                "RC" -> ReturnCodeMessage(args[1].toInt())
                else -> throw Exception("Incorrect message: $args")
            }
        }
    }
    abstract fun serialize() : String
}

data class FullStateMessage(val objs: GameObjects) : ServerMessage() {
    override fun serialize(): String = TODO("not implemented")
}

data class StateDifferenceMessage(val diff: StateDifference) : ServerMessage() {
    override fun serialize(): String = TODO("not implemented")
}

/**
 * Изменение карты (новая карта полностью чистая, после этого сообщения должны идти сообщения добавления объектов)
 */
data class MapChangeMessage(val mapName: String) : ServerMessage() {
    override fun serialize(): String = "MC$INNER_DLMTR$mapName"
}

data class ReturnCodeMessage(val returnCode: Int) : ServerMessage() {
    override fun serialize(): String = "RC$INNER_DLMTR$returnCode"
}