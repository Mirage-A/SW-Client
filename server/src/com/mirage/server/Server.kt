package com.mirage.server

import com.badlogic.gdx.net.Socket
import com.mirage.utils.messaging.ClientMessage
import javax.swing.SwingUtilities

object Server {

    private val rooms = ArrayList<Room>()

    private val playerMessageListener : (ClientMessage) -> Unit = {
        //TODO
    }

    private val playerDisconnectListener : (Player) -> Unit = {
        it.room?.disconnectPlayer(it)
        sendAdminMessage(PlayerDisconnectedAdminMessage(it))
    }

    private fun newSocketListener(socket: Socket) {
        val player = Player(socket, playerMessageListener, playerDisconnectListener)
        val room = selectRoomForConnectedPlayer(player)
        room.addPlayer(player)
        sendAdminMessage(PlayerConnectedAdminMessage(player))
    }

    /**
     * Определяет, в какую комнату нужно направить подключившегося игрока.
     * Создаёт новую комнату, если это необходимо, и возвращает ссылку на комнату.
     * //TODO
     */
    private fun selectRoomForConnectedPlayer(player: Player) : Room {
        return rooms[0]
    }

    private val socketFactory = SocketFactory(::newSocketListener)

    init {
        rooms.add(Room())
        rooms.add(Room())
        sendAdminMessage(RoomAddedAdminMessage(rooms[0]))
        sendAdminMessage(RoomAddedAdminMessage(rooms[1]))
        socketFactory.start()
    }


    var adminMessageListener: ((AdminMessage) -> Unit)? = null

    private fun sendAdminMessage(msg: AdminMessage) {
        SwingUtilities.invokeLater {
            adminMessageListener?.invoke(msg)
        }
    }
}