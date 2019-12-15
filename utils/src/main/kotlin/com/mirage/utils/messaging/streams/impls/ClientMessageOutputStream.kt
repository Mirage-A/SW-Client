package com.mirage.utils.messaging.streams.impls

import com.mirage.utils.OUTER_DLMTR
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.serializeClientMessage
import com.mirage.utils.messaging.streams.ClientMessageWriter
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