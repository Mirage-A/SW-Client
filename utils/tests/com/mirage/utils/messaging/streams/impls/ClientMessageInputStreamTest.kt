package com.mirage.utils.messaging.streams.impls

import com.badlogic.gdx.Net
import com.badlogic.gdx.net.NetJavaServerSocketImpl
import com.badlogic.gdx.net.NetJavaSocketImpl
import com.badlogic.gdx.net.ServerSocketHints
import com.badlogic.gdx.net.SocketHints
import com.mirage.utils.INNER_DLMTR
import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.StringReader

internal class ClientMessageInputStreamTest {

    @Test
    fun read() {
        val str = "MSG$INNER_DLMTR@ELL@$INNER_DLMTR#HI#\nNEWMSG$INNER_DLMTR%HELL%"
        val input = str.byteInputStream()
        val deserializer = ClientMessageInputStream(input)
        println(deserializer.read())
        println(deserializer.read())

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
            println("hi!")
            val reader = ClientMessageInputStream(player.inputStream)
            val out = client.outputStream
            launch {
                out.write("MSG$INNER_DLMTR@ELL@$INNER_DLMTR#HI#\nNEWMSG$INNER_DLMTR%HELL%\nHELLO\n".toByteArray())
                out.flush()
            }
            launch {
                println(reader.read())
                println(reader.read())
                println(reader.read())
            }
        }
    }
}