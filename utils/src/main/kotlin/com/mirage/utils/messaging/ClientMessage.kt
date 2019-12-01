package com.mirage.utils.messaging

import com.mirage.utils.game.objects.properties.MoveDirection


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
                ReconnectClientMessage::class.java,
                ChangeSceneClientMessage::class.java,
                ExitClientMessage::class.java
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

data class MoveDirectionClientMessage(val md: MoveDirection) : ClientMessage()

data class SetMovingClientMessage(val isMoving: Boolean) : ClientMessage()

data class RegisterClientMessage(val nickname: String, val login: String, val password: String): ClientMessage()

data class LoginClientMessage(val login: String, val password: String) : ClientMessage()

data class CityJoinClientMessage(val cityID: Long) : ClientMessage()

data class ReconnectClientMessage(val roomID: Long) : ClientMessage()

/**
 * Сообщение о смене экрана.
 * Это сообщение создаётся в модуле UI, обрабатывается клиентом и не передаётся серверу.
 */
data class ChangeSceneClientMessage(val newScene: Scene) : ClientMessage() {

    enum class Scene {
        MAIN_MENU,
        SETTINGS_MENU,
        MULTIPLAYER_LOBBY,
        SINGLEPLAYER_GAME,
        MULTIPLAYER_GAME
    }

}
/**
 * Сообщение о выходе из игры.
 * Это сообщение создаётся в модуле UI, обрабатывается клиентом и не передаётся серверу.
 */
data class ExitClientMessage(val exitCode: Int) : ClientMessage()