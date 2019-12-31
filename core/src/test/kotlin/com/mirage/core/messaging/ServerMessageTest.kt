package com.mirage.core.messaging

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ServerMessageTest {

    @Test
    fun testMessagesListValidness() {
        for (clazz in ServerMessage.classToCodeMap.keys) {
            assertEquals("com.mirage.core.messaging.ServerMessage", clazz.superclass.name)
            assertEquals(clazz, ServerMessage.codeToClassMap[ServerMessage.classToCodeMap[clazz]])
        }
    }
}