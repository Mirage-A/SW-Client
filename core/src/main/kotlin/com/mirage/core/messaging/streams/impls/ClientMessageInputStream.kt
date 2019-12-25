package com.mirage.core.messaging.streams.impls

import com.mirage.core.messaging.ClientMessage
import com.mirage.core.messaging.deserializeClientMessage
import com.mirage.core.messaging.streams.ClientMessageReader
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class ClientMessageInputStream(inputStream: InputStream) : ClientMessageReader {

    private val reader = BufferedReader(InputStreamReader(inputStream))

    override fun read(): ClientMessage? = deserializeClientMessage(reader.readLine())

}