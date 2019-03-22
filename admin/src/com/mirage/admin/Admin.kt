package com.mirage.admin

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

    init {
        val gui = AdminGUI(LinkedList())
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
                }
                else {
                    gui.printlnInTerminal("Failed to start server.")
                }
            }
        })
    }
}