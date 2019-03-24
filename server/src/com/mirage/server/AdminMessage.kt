package com.mirage.server

sealed class AdminMessage

class ServerErrorAdminMessage(val errorMsg: String): AdminMessage()

class RoomAddedAdminMessage(val room: Room) : AdminMessage()

class RoomClosedAdminMessage(val room: Room) : AdminMessage()

class PlayerConnectedAdminMessage(val player: Player) : AdminMessage()

class PlayerDisconnectedAdminMessage(val player: Player) : AdminMessage()