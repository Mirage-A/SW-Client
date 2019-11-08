package com.mirage.utils.messaging

import com.mirage.utils.INNER_DLMTR
import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.game.states.StateDifference

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
                "MC" -> TODO("not implemented")
                "RC" -> ReturnCodeMessage(args[1].toInt())
                else -> throw Exception("Incorrect message: $args")
            }
        }
    }
    abstract fun serialize() : String
}

/**
 * Изменение карты на карту с названием [mapName] и изначальным состоянием объектов [initialObjects]
 * [stateCreatedTimeMillis] - время, в которое было создано состояние [initialObjects]
 */
data class MapChangeMessage(val mapName: String, val initialObjects: GameObjects, val stateCreatedTimeMillis: Long) : ServerMessage() {
    override fun serialize(): String = TODO("not implemented")
}

data class StateDifferenceMessage(val diff: StateDifference, val stateCreatedTimeMillis: Long) : ServerMessage() {
    override fun serialize(): String = TODO("not implemented")
}


data class ReturnCodeMessage(val returnCode: Int) : ServerMessage() {
    override fun serialize(): String = "RC$INNER_DLMTR$returnCode"
}