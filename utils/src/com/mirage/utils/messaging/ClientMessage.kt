package com.mirage.utils.messaging

sealed class ClientMessage

data class MoveDirectionClientMessage(val md: MoveDirection) : ClientMessage()

data class SetMovingClientMessage(val isMoving: Boolean) : ClientMessage()