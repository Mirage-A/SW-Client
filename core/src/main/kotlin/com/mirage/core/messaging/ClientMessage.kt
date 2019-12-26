package com.mirage.core.messaging

import com.mirage.core.datastructures.Point
import com.mirage.core.extensions.EntityID
import com.mirage.core.game.objects.properties.MoveDirection


sealed class ClientMessage {
    companion object {
        /** List of all classes inherited from [ClientMessage] */
        private val clientMessageClasses = listOf<Class<*>>(
                MoveDirectionClientMessage::class.java,
                SetMovingClientMessage::class.java,
                CastSkillClientMessage::class.java,
                InteractionClientMessage::class.java,
                SetTargetClientMessage::class.java,
                RegisterClientMessage::class.java,
                LoginClientMessage::class.java,
                CityJoinClientMessage::class.java,
                ReconnectClientMessage::class.java,
                ChangeSceneClientMessage::class.java,
                ExitClientMessage::class.java,
                NewTargetMessage::class.java,
                ClearTargetMessage::class.java
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

/** Message of trying to cast a spell
 * @param skillID An index of a spell on player's skill pane
 * @param targetID ID of a target entity
 */
data class CastSkillClientMessage(val skillID: Int, val targetID: Long?) : ClientMessage()

/** Message of trying to interact with entity [entityID] through interaction button */
data class InteractionClientMessage(val entityID: EntityID) : ClientMessage()

data class SetTargetClientMessage(val targetID: EntityID?): ClientMessage()

data class RegisterClientMessage(val nickname: String, val login: String, val password: String): ClientMessage()

data class LoginClientMessage(val login: String, val password: String) : ClientMessage()

data class CityJoinClientMessage(val cityID: Long) : ClientMessage()

data class ReconnectClientMessage(val roomID: Long) : ClientMessage()

/** This message is handled by client and does not reach game logic */
data class ChangeSceneClientMessage(val newScene: Scene) : ClientMessage() {

    enum class Scene {
        MAIN_MENU,
        LOADING_SCREEN,
        NEW_PROFILE_MENU,
        SETTINGS_MENU,
        MULTIPLAYER_LOBBY,
        SINGLEPLAYER_GAME,
        MULTIPLAYER_GAME
    }

}
/** This message is handled by client and does not reach game logic */
data class ExitClientMessage(val exitCode: Int) : ClientMessage()

/** This message is handled by client and does not reach game logic */
data class NewTargetMessage(val virtualScreenPoint: Point) : ClientMessage()

/** This message is handled by client and does not reach game logic */
data class ClearTargetMessage(val unit: Unit = Unit) : ClientMessage()

/** This message is handled by client and does not reach game logic */
data class CloseConnectionMessage(val unit: Unit = Unit) : ClientMessage()