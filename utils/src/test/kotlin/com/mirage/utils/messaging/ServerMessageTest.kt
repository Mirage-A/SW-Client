package com.mirage.utils.messaging

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class ServerMessageTest {

    @Test
    fun testMessagesListValidness() {
        for (clazz in ServerMessage.classToCodeMap.keys) {
            assertEquals("com.mirage.utils.messaging.ServerMessage", clazz.superclass.name)
            assertEquals(clazz, ServerMessage.codeToClassMap[ServerMessage.classToCodeMap[clazz]])
        }
    }
}