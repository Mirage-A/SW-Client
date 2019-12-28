package com.mirage.core.messaging

import com.mirage.core.INNER_DLMTR
import com.mirage.core.OUTER_DLMTR
import com.mirage.core.game.objects.properties.MoveDirection
import com.mirage.core.utils.TestSamples
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MessageSerializationKtTest {

    @Test
    fun testServerMessaging() {
        val msg = ReturnCodeMessage(1)
        assertEquals(msg, deserializeServerMessage(serializeServerMessage(msg)))
    }

    @Test
    fun testServerMessagingAgain() {
        val msg = InitialGameStateMessage(
                "mapName",
                TestSamples.TEST_TWO_GAME_OBJECTS.simplifiedDeepCopy(),
                0L,
                0L
        )
        val serialized = serializeServerMessage(msg)
        val deserialized = deserializeServerMessage(serialized)
        assertEquals(msg, deserialized)
    }

    @Test
    fun testServerExceptions() {
        assertDoesNotThrow {
            deserializeServerMessage("")
            deserializeServerMessage("!!!!!!")
            deserializeServerMessage("$INNER_DLMTR $OUTER_DLMTR")
            deserializeServerMessage("""0$INNER_DLMTR{"isMoving":true}""")
            deserializeServerMessage("""${Int.MAX_VALUE}$INNER_DLMTR{"isMoving":true}""")
        }
    }

    @Test
    fun testClientMessaging() {
        val msg = SetMovingClientMessage(true)
        assertEquals(msg, deserializeClientMessage(serializeClientMessage(msg)))
    }

    @Test
    fun testClientMessagingAgain() {
        val msg = MoveDirectionClientMessage(MoveDirection.DOWN_LEFT)
        val serialized = serializeClientMessage(msg)
        val deserialized = deserializeClientMessage(serialized)
        assertEquals(msg, deserialized)
    }

    @Test
    fun testClientExceptions() {
        assertDoesNotThrow {
            deserializeClientMessage("")
            deserializeClientMessage("!!!!!!")
            deserializeClientMessage("$INNER_DLMTR $OUTER_DLMTR")
            deserializeClientMessage("""0$INNER_DLMTR{"returnCode":1}""")
            deserializeClientMessage("""${Int.MAX_VALUE}$INNER_DLMTR{"returnCode":1}""")
        }
    }
}