package com.mirage.server

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.net.Socket
import com.mirage.utils.Assets
import com.mirage.utils.Timer
import com.mirage.utils.messaging.CityJoinClientMessage
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.LoginClientMessage
import com.mirage.utils.messaging.ReturnCodeMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import java.io.File
import javax.swing.SwingUtilities

/**
 * Синглтон, представляющий собой сервер.
 * Предоставляет слушателей для обработки различных событий, таких как:
 *  - Получение сообщения от игрока //TODO Возможно, стоит перенести в другое место
 *  - Отключение игрока
 *  - Подключение игрока
 * Содержит коллекцию комнат, по которым распределяет игроков
 * @see Room
 * @see ClientMessage
 * @see AdminMessage
 */
object Server {

    init {
        println(Thread.currentThread().toString())
        println(Thread.currentThread().id)
        CoroutineScope(newSingleThreadContext("hello")).launch {
            println(Thread.currentThread().toString())
            println(Thread.currentThread().id)
        }

    }
    private val rooms : MutableCollection<Room> = ArrayList()
    private val playersWithoutRoom : MutableList<Player> = ArrayList()

    private val playerMessageListener : (Player, ClientMessage) -> Unit = { player, msg ->
        //TODO
        when (msg) {
            is LoginClientMessage -> {
                player.sendMessage(ReturnCodeMessage(0))
            }
            is CityJoinClientMessage -> {
                player.sendMessage(ReturnCodeMessage(100))
                //TODO Выбор комнаты на основании id города в сообщении
                if (rooms.isEmpty()) {
                    val room = Room()
                    room.start() //TODO Надо это вызывать в главном потоке с OpenGL-контекстом((((

                    room.addPlayer(player)
                    sendAdminMessage(RoomAddedAdminMessage(room))
                }
                else {
                    rooms.first().addPlayer(player)
                }
            }
        }
    }

    private val playerDisconnectListener : (Player) -> Unit = {
        it.room?.disconnectPlayer(it)
        sendAdminMessage(PlayerDisconnectedAdminMessage(it))
    }

    private fun newSocketListener(socket: Socket) {
        val player = Player(socket, playerMessageListener, playerDisconnectListener)
        playersWithoutRoom.add(player)
        sendAdminMessage(PlayerConnectedAdminMessage(player))
    }

    private val socketFactory = SocketFactory(::newSocketListener)

    private val outOfRoomPlayersThread = Timer(100) {
        for (player in playersWithoutRoom) {
            player.checkNewMessages()
        }
    }

    init {
        socketFactory.start()
        outOfRoomPlayersThread.start()
    }


    var adminMessageListener: ((AdminMessage) -> Unit)? = null

    private fun sendAdminMessage(msg: AdminMessage) {
        SwingUtilities.invokeLater {
            adminMessageListener?.invoke(msg)
        }
    }
}