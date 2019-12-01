package com.mirage.utils.messaging.streams.impls

import com.badlogic.gdx.Net
import com.badlogic.gdx.net.NetJavaServerSocketImpl
import com.badlogic.gdx.net.NetJavaSocketImpl
import com.badlogic.gdx.net.ServerSocketHints
import com.badlogic.gdx.net.SocketHints
import com.mirage.utils.game.oldobjects.GameObject
import com.mirage.utils.messaging.MoveDirectionClientMessage
import com.mirage.utils.messaging.SetMovingClientMessage
import kotlinx.coroutines.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ClientMessageOutputStreamTest {

    @Test
    fun ioTest() {
        val serverSocket = NetJavaServerSocketImpl(Net.Protocol.TCP, 55555, ServerSocketHints())
        val playerAsync = GlobalScope.async {
            serverSocket.accept(SocketHints())
        }
        val cliAsync = GlobalScope.async {
            delay(10)
            NetJavaSocketImpl(Net.Protocol.TCP, "localhost", 55555, SocketHints())
        }
        runBlocking {
            val player = playerAsync.await()
            val client = cliAsync.await()
            val reader = ClientMessageInputStream(player.inputStream)
            val out = ClientMessageOutputStream(client.outputStream)
            launch {
                out.write(MoveDirectionClientMessage(GameObject.MoveDirection.DOWN_LEFT))
                out.write(SetMovingClientMessage(false))
                out.flush()
            }
            launch {
                assertEquals(MoveDirectionClientMessage(GameObject.MoveDirection.DOWN_LEFT), reader.read())
                assertEquals(SetMovingClientMessage(false), reader.read())
            }
        }
    }
}