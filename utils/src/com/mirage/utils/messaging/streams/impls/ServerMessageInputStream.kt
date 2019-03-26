package com.mirage.utils.messaging.streams.impls

import com.mirage.utils.LOG_ALL_MESSAGES
import com.mirage.utils.messaging.*
import com.mirage.utils.messaging.streams.ServerMessageReader
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Обёртка над потоком [InputStream].
 * Используется для десериализации сообщений, отправленных с помощью [ServerMessageOutputStream].
 */
class ServerMessageInputStream(inputStream: InputStream) : ServerMessageReader {

    private val reader = BufferedReader(InputStreamReader(inputStream))

    override fun read(): ServerMessage {
        val msg = ServerMessage.deserialize(reader.readLine())
        if (LOG_ALL_MESSAGES) {
            println("Got $msg")
        }
        return msg
    }

}