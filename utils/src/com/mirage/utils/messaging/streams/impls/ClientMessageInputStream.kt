package com.mirage.utils.messaging.streams.impls

import com.mirage.utils.INNER_DLMTR
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.MoveDirection
import com.mirage.utils.messaging.MoveDirectionClientMessage
import com.mirage.utils.messaging.streams.ClientMessageReader
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Обёртка над потоком InputStream.
 * Используется для десериализации сообщений, отправленных с помощью ClientMessageOutputStream.
 */
class ClientMessageInputStream(inputStream: InputStream) : ClientMessageReader {

    private val reader = BufferedReader(InputStreamReader(inputStream))

    override fun read(): ClientMessage {//TODO Десериализация
        val str = reader.readLine()
        val args = str.split(INNER_DLMTR)
        println(args)
        return MoveDirectionClientMessage(MoveDirection.DOWN)
    }
}