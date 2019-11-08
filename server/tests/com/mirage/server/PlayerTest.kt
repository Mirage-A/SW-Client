package com.mirage.server

import com.badlogic.gdx.Net
import com.badlogic.gdx.net.NetJavaServerSocketImpl
import com.badlogic.gdx.net.NetJavaSocketImpl
import com.badlogic.gdx.net.ServerSocketHints
import com.badlogic.gdx.net.SocketHints
import com.mirage.utils.game.objects.MoveDirection
import com.mirage.utils.messaging.*
import com.mirage.utils.messaging.streams.impls.ClientMessageOutputStream
import com.mirage.utils.messaging.streams.impls.ServerMessageInputStream
import kotlinx.coroutines.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.BufferedWriter
import java.io.OutputStreamWriter

internal class PlayerTest {

    @Test
    fun testMessaging() {
        val serverSocket = NetJavaServerSocketImpl(Net.Protocol.TCP, 55555, ServerSocketHints())
        val playerAsync = GlobalScope.async {
            serverSocket.accept(SocketHints())
        }
        val cliAsync = GlobalScope.async {
            delay(10)
            NetJavaSocketImpl(Net.Protocol.TCP, "localhost", 55555, SocketHints())
        }
        runBlocking {
            val playerSocket = playerAsync.await()
            val clientSocket = cliAsync.await()
            val playerGotMessages = ArrayList<ClientMessage>()
            var disconnected = false
            val player = Player(playerSocket, {_, msg ->
                playerGotMessages.add(msg)
            }, {
                disconnected = true
            })
            player.sendMessage(MapChangeMessage("1"))
            player.sendMessage(ReturnCodeMessage(2))
            val clientWriter = ClientMessageOutputStream(clientSocket.outputStream)
            clientWriter.write(SetMovingClientMessage(false))
            player.sendMessage(RemoveObjectMessage(3))
            clientWriter.write(MoveDirectionClientMessage(MoveDirection.DOWN))
            clientWriter.flush()
            delay(100L)
            player.checkNewMessages()
            assertEquals(SetMovingClientMessage(false), playerGotMessages[0])
            assertEquals(MoveDirectionClientMessage(MoveDirection.DOWN), playerGotMessages[1])
            val clientReader = ServerMessageInputStream(clientSocket.inputStream)
            assertEquals(MapChangeMessage("1"), clientReader.read())
            assertEquals(ReturnCodeMessage(2), clientReader.read())
            assertEquals(RemoveObjectMessage(3), clientReader.read())
            val out = BufferedWriter(OutputStreamWriter(clientSocket.outputStream))
            assertFalse(disconnected)
            out.write("fndsgnski\n")
            out.flush()
            delay(100L)
            assertTrue(disconnected)
        }
    }
}