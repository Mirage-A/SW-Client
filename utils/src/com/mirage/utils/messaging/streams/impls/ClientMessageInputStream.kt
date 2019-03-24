package com.mirage.utils.messaging.streams.impls

import com.mirage.utils.INNER_DLMTR
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.MoveDirection
import com.mirage.utils.messaging.MoveDirectionClientMessage
import com.mirage.utils.messaging.SetMovingClientMessage
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

    override fun read(): ClientMessage {
        val str = reader.readLine()
        val args = str.split(INNER_DLMTR)
        return when (args[0]) {
            "MD" -> MoveDirectionClientMessage(MoveDirection.fromString(args[1]))
            "MV" -> SetMovingClientMessage(args[1].toBoolean())
            else -> throw Exception("Incorrect message: $args")
        }
    }

}