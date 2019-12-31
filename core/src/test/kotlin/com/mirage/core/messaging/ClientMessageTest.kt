package com.mirage.core.messaging

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ClientMessageTest {

    @Test
    fun testMessagesListValidness() {
        for (clazz in ClientMessage.classToCodeMap.keys) {
            assertEquals("com.mirage.core.messaging.ClientMessage", clazz.superclass.name)
            assertEquals(clazz, ClientMessage.codeToClassMap[ClientMessage.classToCodeMap[clazz]])
        }
    }

}