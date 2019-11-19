package com.mirage.utils.messaging.streams.impls

import com.mirage.utils.LOG_ALL_MESSAGES
import com.mirage.utils.messaging.ServerMessage
import com.mirage.utils.messaging.deserializeServerMessage
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

    override fun read(): ServerMessage? = deserializeServerMessage(reader.readLine())

}