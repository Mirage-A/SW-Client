package com.mirage.core.messaging.streams.impls

import com.mirage.core.messaging.ServerMessage
import com.mirage.core.messaging.deserializeServerMessage
import com.mirage.core.messaging.streams.ServerMessageReader
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class ServerMessageInputStream(inputStream: InputStream) : ServerMessageReader {

    private val reader = BufferedReader(InputStreamReader(inputStream))

    override fun read(): ServerMessage? = deserializeServerMessage(reader.readLine())

}