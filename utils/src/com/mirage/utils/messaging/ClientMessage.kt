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
                "MV" -> SetMovingClientMessage(args[1].toBoolean())
                "RG" -> RegisterClientMessage(args[1], args[2], args[3])
                "LG" -> LoginClientMessage(args[1], args[2])
                "CJ" -> CityJoinClientMessage(args[1].toLong())
                "RC" -> ReconnectClientMessage(args[1].toLong())
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

data class RegisterClientMessage(val nickname: String, val login: String, val password: String): ClientMessage() {
    override fun serialize(): String = "RG$INNER_DLMTR$nickname$INNER_DLMTR$login$INNER_DLMTR$password"
}

data class LoginClientMessage(val login: String, val password: String) : ClientMessage() {
    override fun serialize(): String = "LG$INNER_DLMTR$login$INNER_DLMTR$password"
}

data class CityJoinClientMessage(val cityID: Long) : ClientMessage() {
    override fun serialize(): String = "CJ$INNER_DLMTR$cityID"
}

data class ReconnectClientMessage(val roomID: Long) : ClientMessage() {
    override fun serialize(): String = "RC$INNER_DLMTR$roomID"
}