package com.mirage.utils.messaging.streams.impls

import com.mirage.utils.OUTER_DLMTR
import com.mirage.utils.messaging.ServerMessage
import com.mirage.utils.messaging.serializeServerMessage
import com.mirage.utils.messaging.streams.ServerMessageWriter
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