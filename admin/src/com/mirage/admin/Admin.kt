package com.mirage.admin

import com.mirage.server.*
import java.util.*
import javax.swing.JFrame

class Admin : JFrame() {

    private val rooms = LinkedList<Room>()

    private val gui = AdminGUI(rooms)

    init {
        gui.setTerminalInputListener {
            if (it.startsWith("/")) {
                if (it.startsWith("/mock")) {
                    Server.createMockClient()
                    gui.printlnInTerminal("Created a mock player")
                }
            }
            gui.clearInput()
        }
    }

    init {
        Server.adminMessageListener = {
            println("Got message! $it")
            when (it) {
                //TODO Обработка сообщений
            }
        }
    }
}