package com.mirage.server

import com.badlogic.gdx.Net
import com.badlogic.gdx.net.NetJavaSocketImpl
import com.badlogic.gdx.net.SocketHints
import com.mirage.utils.SERVER_ADDRESS
import com.mirage.utils.SERVER_PORT
import javax.swing.SwingUtilities

object Server {

    private val rooms = ArrayList<Room>()

    private val socketFactory = SocketFactory {
        //TODO Распределение сокетов
    }

    fun createMockClient() =
        NetJavaSocketImpl(Net.Protocol.TCP, SERVER_ADDRESS, SERVER_PORT, SocketHints())

    init {
        socketFactory.start()
    }


    var adminMessageListener: ((AdminMessage) -> Unit)? = null

    fun sendAdminMessage(msg: AdminMessage) {
        SwingUtilities.invokeLater {
            adminMessageListener?.invoke(msg)
        }
    }
}