package com.mirage.utils.messaging

import com.mirage.utils.INNER_DLMTR

sealed class ClientMessage {
    companion object {
        /**
         * Десериализует сообщение из строки.
         * @throws Exception если строка некорректна и была получена не методом serialize
         * @see serialize
         */
        fun deserialize(str: String) : ClientMessage {
            val args = str.split(INNER_DLMTR)
            return when (args[0]) {
                "MD" -> MoveDirectionClientMessage(MoveDirection.fromString(args[1]))
                "MV" -> SetMovingClientMessage(args[1] == "true")
                else -> throw Exception("Incorrect message: $args")
            }
        }
    }
    abstract fun serialize() : String
}

data class MoveDirectionClientMessage(val md: MoveDirection) : ClientMessage() {
    override fun serialize(): String = "MD$INNER_DLMTR$md"
}

data class SetMovingClientMessage(val isMoving: Boolean) : ClientMessage() {
    override fun serialize(): String = "MV$INNER_DLMTR$isMoving"
}