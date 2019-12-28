package com.mirage.core.messaging.streams.impls

import com.mirage.core.messaging.ServerMessage
import com.mirage.core.messaging.serializeServerMessage
import com.mirage.core.messaging.streams.ServerMessageWriter
import com.mirage.core.utils.OUTER_DLMTR
import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter

class ServerMessageOutputStream(outputStream: OutputStream) : ServerMessageWriter {

    private val out = BufferedWriter(OutputStreamWriter(outputStream))

    override fun write(msg: ServerMessage) {
        out.write(serializeServerMessage(msg))
        out.write(OUTER_DLMTR.toString())
    }

    override fun flush() = out.flush()

}