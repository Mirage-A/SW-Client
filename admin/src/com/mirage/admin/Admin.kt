package com.mirage.admin

import com.mirage.server.Player
import com.mirage.server.PlayerStream
import com.mirage.server.Room
import java.awt.event.ActionListener
import java.util.*
import javax.swing.JFrame

class Admin : JFrame() {

    /**
     * Попытка запустить сервер. Возвращает true, если сервер был запущен.
     */
    private fun startServer() : Boolean {
        return true
    }

    /**
     * Попытка остановить сервер. Возвращает true, если сервер был остановлен.
     */
    private fun stopServer() : Boolean {
        return true
    }

    private var serverOnline = false

    private val rooms = LinkedList<Room>()

    init {
        val gui = AdminGUI(rooms)
        gui.setTerminalInputListener {
            gui.printlnInTerminal(it)
            gui.clearInput()
        }
        gui.setServerStatusBtnListener(ActionListener {
            if (serverOnline) {
                if (stopServer()) {
                    gui.setServerStatus(false)
                    serverOnline = false
                    gui.printlnInTerminal("Server successfully stopped.")
                }
                else {
                    gui.printlnInTerminal("Failed to stop server.")
                }
            }
            else {
                if (startServer()) {
                    gui.setServerStatus(true)
                    serverOnline = true
                    gui.printlnInTerminal("Server successfully started.")
                    rooms.add(Room().apply {
                        addPlayer(Player(Random().nextLong(), PlayerStream("localhost")))
                    })
                    gui.updateRoomsList()
                }
                else {
                    gui.printlnInTerminal("Failed to start server.")
                }
            }
        })
    }
}