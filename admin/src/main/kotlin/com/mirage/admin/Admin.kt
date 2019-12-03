package com.mirage.admin

import com.badlogic.gdx.Net
import com.badlogic.gdx.net.NetJavaSocketImpl
import com.badlogic.gdx.net.Socket
import com.badlogic.gdx.net.SocketHints
import com.mirage.server.*
import com.mirage.utils.SERVER_ADDRESS
import com.mirage.utils.SERVER_PORT
import com.mirage.utils.game.objects.properties.MoveDirection
import com.mirage.utils.messaging.MoveDirectionClientMessage
import com.mirage.utils.messaging.streams.impls.ClientMessageOutputStream
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.swing.JFrame

internal class Admin : JFrame() {


    private fun adminMessageListener(msg: AdminMessage) {
        when (msg) {
            is RoomAddedAdminMessage -> {
                rooms.add(msg.room)
                gui.updateRoomsList()
            }
            is PlayerConnectedAdminMessage -> {
                gui.printlnInTerminal("Player connected: ${msg.player}")
                gui.updatePlayersInRoomList()
                gui.updatePlayersOnlineCount()
            }
            is PlayerDisconnectedAdminMessage -> {
                gui.printlnInTerminal("Player disconnected: ${msg.player}")
                gui.updatePlayersInRoomList()
                gui.updatePlayersOnlineCount()
            }
            //TODO Обработка сообщений
        }
    }

    private val rooms = LinkedList<Room>()

    private val gui = AdminGUI(rooms)

    private val mockPlayers = LinkedList<Socket>()

    private val terminalInputListener : (String) -> Unit = {
        try {
            when {
                it.isBlank() -> {
                }
                it == "help" -> {
                    gui.printlnInTerminal("Available commands:\n" +
                            "---- echo [text]: Print [text] in terminal\n" +
                            "---- mock: Create a mock player (test only)\n" +
                            "---- mocks [count]: Create [count] mock players (stress test only)\n" +
                            "---- killmocks: Disconnect all mock players\n" +
                            "---- ddos [count]: Forces every mock player to send [count] messages to server (stress test only)\n" +
                            "---- help: Print this list\n" +
                            "---- shutdown: Shutdown server")
                }
                it == "mock" -> {
                    mockPlayers.add(NetJavaSocketImpl(Net.Protocol.TCP, SERVER_ADDRESS, SERVER_PORT, SocketHints()))
                    gui.printlnInTerminal("Created a mock player.")
                }
                it.startsWith("mocks ") && it.length > 6 -> {
                    val count = try {
                        Integer.parseInt(it.substring(6).trim())
                    }
                    catch (ex: NumberFormatException) {
                        gui.printError("ERROR: Command argument must be a number.")
                        0
                    }
                    for (i in 0 until count) {
                        mockPlayers.add(NetJavaSocketImpl(Net.Protocol.TCP, SERVER_ADDRESS, SERVER_PORT, SocketHints()))
                    }
                    gui.printlnInTerminal("Created $count mock players.")
                }
                it.startsWith("ddos ") && it.length > 5 -> {
                    val count = try {
                        Integer.parseInt(it.substring(5).trim())
                    }
                    catch (ex: NumberFormatException) {
                        gui.printError("ERROR: Command argument must be a number.")
                        0
                    }
                    for (mock in mockPlayers) {
                        val out = ClientMessageOutputStream(mock.outputStream)
                        for (i in 0 until count) {
                            println(i)
                            out.write(MoveDirectionClientMessage(MoveDirection.RIGHT))
                        }
                        println("GO!")
                        out.flush()
                    }
                    gui.printlnInTerminal("DDOSing server with $count messages!")
                }
                it == "killmocks" -> {
                    for (pl in mockPlayers) {
                        pl.dispose()
                    }
                    mockPlayers.clear()
                    gui.printlnInTerminal("Killed all mock players.")
                }
                it.startsWith("echo ") -> {
                    gui.printlnInTerminal(it.substring(5))
                }
                it == "shutdown" -> {
                    gui.printlnInTerminal("Why?.... :(")
                    GlobalScope.launch {
                        delay(1000)
                        System.exit(0)
                    }
                }
                else -> {
                    gui.printlnInTerminal("Unknown command. Type 'help' to get list of available commands.")
                }
            }
        }
        catch(ex: Exception) {
            gui.printlnInTerminal("ERROR: Your command caused an exception.\n")
        }
        gui.clearInput()
    }

    init {
        Server.adminMessageListener = ::adminMessageListener
        gui.setTerminalInputListener(terminalInputListener)
    }
}