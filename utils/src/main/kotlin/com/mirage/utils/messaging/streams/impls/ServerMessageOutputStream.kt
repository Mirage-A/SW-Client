package com.mirage.utils.messaging.streams.impls

import com.mirage.utils.OUTER_DLMTR
import com.mirage.utils.messaging.ServerMessage
import com.mirage.utils.messaging.streams.ServerMessageWriter
import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter

/**
 * Обёртка над потоком [OutputStream].
 * Сериализует сообщения и отправляет их в поток [outputStream].
 * @see [ServerMessageInputStream]
 */
class ServerMessageOutputStream(outputStream: OutputStream) : ServerMessageWriter {

    private val out = BufferedWriter(OutputStreamWriter(outputStream))

    override fun write(msg: ServerMessage) {
        println("Sending $msg")
        out.write(msg.serialize())
        out.write(OUTER_DLMTR)
    }

    override fun flush() = out.flush()

}