package com.mirage.utils.messaging.streams.impls

import com.mirage.utils.INNER_DLMTR
import com.mirage.utils.OUTER_DLMTR
import com.mirage.utils.messaging.ClientMessage
import com.mirage.utils.messaging.MoveDirectionClientMessage
import com.mirage.utils.messaging.SetMovingClientMessage
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
        val str = buildString {
            when (msg) {
                is MoveDirectionClientMessage -> {
                    append("MD")
                    append(INNER_DLMTR)
                    append(msg.md.toString())
                }
                is SetMovingClientMessage -> {
                    append("MV")
                    append(INNER_DLMTR)
                    append(msg.isMoving.toString())
                }
            }
            append(OUTER_DLMTR)
        }
        out.write(str)
    }

    override fun flush() = out.flush()

}