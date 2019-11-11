package com.mirage.server

import org.junit.jupiter.api.Test

internal class PlayerTest {

    @Test
    fun testMessaging() {

        //TODO Раскомментить, когда будет реализована сериализация сообщений
        /*
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
            player.sendMessage(InitialGameStateMessage("1", TestSamples.TEST_TWO_GAME_OBJECTS, 0L, 0L))
            player.sendMessage(ReturnCodeMessage(2))
            val clientWriter = ClientMessageOutputStream(clientSocket.outputStream)
            clientWriter.write(SetMovingClientMessage(false))
            clientWriter.write(MoveDirectionClientMessage(GameObject.MoveDirection.DOWN))
            clientWriter.flush()
            delay(100L)
            player.checkNewMessages()
            assertEquals(SetMovingClientMessage(false), playerGotMessages[0])
            assertEquals(MoveDirectionClientMessage(GameObject.MoveDirection.DOWN), playerGotMessages[1])
            val clientReader = ServerMessageInputStream(clientSocket.inputStream)
            assertEquals(InitialGameStateMessage("1", TestSamples.TEST_TWO_GAME_OBJECTS, 0L, 0L), clientReader.read())
            assertEquals(ReturnCodeMessage(2), clientReader.read())
            val out = BufferedWriter(OutputStreamWriter(clientSocket.outputStream))
            assertFalse(disconnected)
            out.write("fndsgnski\n")
            out.flush()
            delay(100L)
            assertTrue(disconnected)
        }

         */
    }
}