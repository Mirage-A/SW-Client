package com.mirage.core.messaging.streams.impls

import com.mirage.core.OUTER_DLMTR
import com.mirage.core.messaging.ClientMessage
import com.mirage.core.messaging.serializeClientMessage
import com.mirage.core.messaging.streams.ClientMessageWriter
import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter

class ClientMessageOutputStream(outputStream: OutputStream) : ClientMessageWriter {

    private val out = BufferedWriter(OutputStreamWriter(outputStream))

    override fun write(msg: ClientMessage) {
        out.write(serializeClientMessage(msg))
        out.write(OUTER_DLMTR.toString())
    }

    override fun flush() = out.flush()

}