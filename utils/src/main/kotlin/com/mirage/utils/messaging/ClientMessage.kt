package com.mirage.utils.messaging

import com.mirage.utils.INNER_DLMTR
import com.mirage.utils.game.objects.GameObject

sealed class ClientMessage {
    companion object {
        /**
         *
         * Список всех классов - наследников ClientMessage.
         * При добавлении нового класса-наследника он обязательно должен добавляться в этот список.
         * //TODO Можно сделать аннотацию ClientMessage, которой нужно будет помечать наследников.
         * //TODO Тогда этот список будет сгенерирован автоматически.
         */
        private val clientMessageClasses = listOf<Class<*>>(
                MoveDirectionClientMessage::class.java,
                SetMovingClientMessage::class.java,
                RegisterClientMessage::class.java,
                LoginClientMessage::class.java,
                CityJoinClientMessage::class.java,
                ReconnectClientMessage::class.java
        )

        internal val codeToClassMap: Map<Int, Class<*>> = HashMap<Int, Class<*>>().apply {
            for ((index, value) in clientMessageClasses.withIndex()) {
                this[index] = value
            }
        }

        internal val classToCodeMap: Map<Class<*>, Int> = HashMap<Class<*>, Int>().apply {
            for ((index, value) in clientMessageClasses.withIndex()) {
                this[value] = index
            }
        }
    }
}

data class MoveDirectionClientMessage(val md: GameObject.MoveDirection) : ClientMessage()

data class SetMovingClientMessage(val isMoving: Boolean) : ClientMessage()

data class RegisterClientMessage(val nickname: String, val login: String, val password: String): ClientMessage()

data class LoginClientMessage(val login: String, val password: String) : ClientMessage()

data class CityJoinClientMessage(val cityID: Long) : ClientMessage()

data class ReconnectClientMessage(val roomID: Long) : ClientMessage()