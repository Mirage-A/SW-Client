package com.mirage.server

sealed class AdminMessage

class ServerErrorAdminMessage(errorMsg: String): AdminMessage()

class RoomClosedAdminMessage(room: Room) : AdminMessage()