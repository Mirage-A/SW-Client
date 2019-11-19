package com.mirage.utils.messaging.streams.impls

import com.mirage.utils.LOG_ALL_MESSAGES
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.deserializeClientMessage
import com.mirage.utils.messaging.streams.ClientMessageReader
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Обёртка над потоком [InputStream].
 * Используется для десериализации сообщений, отправленных с помощью [ClientMessageOutputStream].
 */
class ClientMessageInputStream(inputStream: InputStream) : ClientMessageReader {

    private val reader = BufferedReader(InputStreamReader(inputStream))

    override fun read(): ClientMessage? = deserializeClientMessage(reader.readLine())

}