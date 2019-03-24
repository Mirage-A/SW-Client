package com.mirage.server

import com.badlogic.gdx.Net
import com.badlogic.gdx.net.*
import com.badlogic.gdx.utils.GdxRuntimeException
import com.mirage.utils.SERVER_ADDRESS
import com.mirage.utils.SERVER_PORT

/**
 * Поток, принимающий новые подключения и генерирующий объекты Socket.
 * Сервер через слушателя обрабатывает новые подключения.
 * //TODO Слушатель вызывается в потоке самой фабрики.
 */
class SocketFactory(private val newSocketListener: (Socket) -> Unit) {

    private val serverHints = ServerSocketHints().apply {
        //TODO Настройка сокета сервера
    }

    private val socketHints = SocketHints().apply {
        //TODO Настройка сокетов игроков
    }

    private val serverSocket: ServerSocket = NetJavaServerSocketImpl(Net.Protocol.TCP, SERVER_PORT, serverHints)

    private val thread = Thread(Runnable {
        while (true) {
            try {
                newSocketListener(serverSocket.accept(socketHints))
            }
            catch(ex: GdxRuntimeException) {//Таймаут ожидания
                println("nobody is connecting..... :(")
            }
            catch(ex: Exception) {
                println("ERROR")
                ex.printStackTrace()
            }
        }
    })

    fun start() = thread.start()

}