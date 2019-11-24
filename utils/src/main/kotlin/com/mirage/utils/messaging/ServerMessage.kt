package com.mirage.utils.messaging

import com.mirage.utils.game.objects.GameObject
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
         *
         * Список всех классов - наследников ServerMessage.
         * При добавлении нового класса-наследника он обязательно должен добавляться в этот список.
         * //TODO Можно сделать аннотацию ServerMessage, которой нужно будет помечать наследников.
         * //TODO Тогда этот список будет сгенерирован автоматически.
         */
        private val serverMessageClasses = listOf<Class<*>>(
                InitialGameStateMessage::class.java,
                GameStateUpdateMessage::class.java,
                ReturnCodeMessage::class.java,
                HumanoidEquipmentUpdateMessage::class.java
        )

        internal val codeToClassMap: Map<Int, Class<*>> = HashMap<Int, Class<*>>().apply {
            for ((index, value) in serverMessageClasses.withIndex()) {
                this[index] = value
            }
        }

        internal val classToCodeMap: Map<Class<*>, Int> = HashMap<Class<*>, Int>().apply {
            for ((index, value) in serverMessageClasses.withIndex()) {
                this[value] = index
            }
        }
    }
}
/*{
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
}*/


/**
 * Сообщение с полной информацией о карте и состоянии на момент подключения клиента.
 * Это сообщение является первым, которое получает клиент при подключении к игре,
 * и больше никогда не отправляется.
 * [stateCreatedTimeMillis] - время, в которое было создано состояние [initialObjects]
 */
data class InitialGameStateMessage(
        val mapName: String,
        val initialObjects: GameObjects,
        val playerID: Long,
        val stateCreatedTimeMillis: Long
) : ServerMessage()

/**
 * Сообщение, которое логика отправляет после каждого тика обновления состояния игры.
 */
data class GameStateUpdateMessage(
        val diff: StateDifference,
        val stateCreatedTimeMillis: Long
) : ServerMessage()

data class ReturnCodeMessage(
        val returnCode: Int
) : ServerMessage()

/**
 * Сообщение об изменении экипировки гуманоида (не обязательно игрока).
 * При получении этого сообщения клиент должен обновить используемые для отрисовки объекта текстуры.
 */
data class HumanoidEquipmentUpdateMessage(
        val objectID: Long,
        val newEquipment: GameObject.HumanoidEquipment
) : ServerMessage()