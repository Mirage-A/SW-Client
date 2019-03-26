package com.mirage.utils.messaging.streams.impls

import com.mirage.utils.OUTER_DLMTR
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.streams.ClientMessageWriter
import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter

/**
 * Обёртка над потоком [OutputStream].
 * Сериализует сообщения и отправляет их в поток [outputStream].
 * @see [ClientMessageInputStream]
 */
class ClientMessageOutputStream(outputStream: OutputStream) : ClientMessageWriter {

    private val out = BufferedWriter(OutputStreamWriter(outputStream))

    override fun write(msg: ClientMessage) {
        println("Sending $msg")
        out.write(msg.serialize())
        out.write(OUTER_DLMTR)
    }

    override fun flush() = out.flush()

}