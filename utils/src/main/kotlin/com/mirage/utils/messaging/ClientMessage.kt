package com.mirage.utils.messaging

import com.mirage.utils.datastructures.Point
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
                CastSkillClientMessage::class.java,
                RegisterClientMessage::class.java,
                LoginClientMessage::class.java,
                CityJoinClientMessage::class.java,
                ReconnectClientMessage::class.java,
                ChangeSceneClientMessage::class.java,
                ExitClientMessage::class.java,
                NewTargetMessage::class.java
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

/** Сообщение о попытке применить навык
 * @param skillID Порядковый номер навыка на панели игрока
 * @param targetID ID сущности - цели игрока
 */
data class CastSkillClientMessage(val skillID: Int, val targetID: Long?) : ClientMessage()

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
        NEW_PROFILE_MENU,
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

/**
 * Сообщение о попытке выбрать новую цель.
 * Это сообщение создаётся в модуле UI, обрабатывается клиентом и не передаётся серверу.
 * @param virtualScreenPoint Точка на виртуальном экране, в которую кликнул игрок.
 */
data class NewTargetMessage(val virtualScreenPoint: Point) : ClientMessage()

/**
 * Сообщение об отмене выбора цели.
 * Это сообщение создаётся в модуле UI, обрабатывается клиентом и не передаётся серверу.
 */
data class ClearTargetMessage(val unit: Unit = Unit) : ClientMessage()