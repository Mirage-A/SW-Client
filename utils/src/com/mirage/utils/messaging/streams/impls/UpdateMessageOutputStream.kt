package com.mirage.utils.messaging.streams.impls

import com.mirage.utils.OUTER_DLMTR
import com.mirage.utils.messaging.UpdateMessage
import com.mirage.utils.messaging.streams.UpdateMessageWriter
import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter

/**
 * Обёртка над потоком [OutputStream].
 * Сериализует сообщения и отправляет их в поток [outputStream].
 * @see [UpdateMessageInputStream]
 */
class UpdateMessageOutputStream(outputStream: OutputStream) : UpdateMessageWriter {

    private val out = BufferedWriter(OutputStreamWriter(outputStream))

    override fun write(msg: UpdateMessage) {
        out.write(msg.serialize())
        out.write(OUTER_DLMTR)
    }

    override fun flush() = out.flush()

}