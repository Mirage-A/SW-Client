package com.mirage.server

import com.badlogic.gdx.Net
import com.badlogic.gdx.net.NetJavaSocketImpl
import com.badlogic.gdx.net.Socket
import com.badlogic.gdx.net.SocketHints
import com.mirage.utils.SERVER_ADDRESS
import com.mirage.utils.SERVER_PORT
import com.mirage.utils.messaging.ClientMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.swing.SwingUtilities

object Server {

    private val rooms = ArrayList<Room>()

    private val playerMessageListener : (ClientMessage) -> Unit = {
        println("got message $it")
        //TODO
    }

    private val playerDisconnectListener : () -> Unit = {
        println("disconnect")
    }

    private fun newSocketListener(socket: Socket) {
        println("got new socket!")
        val player = Player(socket, playerMessageListener, playerDisconnectListener)
        selectRoomForConnectedPlayer(player).addPlayer(player)
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
        sendAdminMessage(RoomAddedAdminMessage(rooms[0]))
        socketFactory.start()

    }


    var adminMessageListener: ((AdminMessage) -> Unit)? = null

    fun sendAdminMessage(msg: AdminMessage) {
        SwingUtilities.invokeLater {
            adminMessageListener?.invoke(msg)
        }
    }
}