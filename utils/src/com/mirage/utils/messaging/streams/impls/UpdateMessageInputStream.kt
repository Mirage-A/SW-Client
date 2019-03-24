package com.mirage.utils.messaging.streams.impls

import com.mirage.utils.messaging.*
import com.mirage.utils.messaging.streams.UpdateMessageReader
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Обёртка над потоком [InputStream].
 * Используется для десериализации сообщений, отправленных с помощью [UpdateMessageOutputStream].
 */
class UpdateMessageInputStream(inputStream: InputStream) : UpdateMessageReader {

    private val reader = BufferedReader(InputStreamReader(inputStream))

    override fun read(): UpdateMessage = UpdateMessage.deserialize(reader.readLine())

}