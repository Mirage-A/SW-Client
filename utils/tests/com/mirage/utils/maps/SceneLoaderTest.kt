package com.mirage.utils.maps

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SceneLoaderTest {

    @Test
    fun loadEntityTemplateTest() {
        val json = """
            {
                "template": "spawn-point",
                "width": 0.25,
                "height": 0.25,
                "moveDirection": "UP_RIGHT",
                "speed": 2.8,
                "isRigid": false,
                "layer": 0
            }
        """.trimIndent()
        val obj = SceneLoader.loadEntityTemplate(json.reader())
        assertEquals(Entity(
                name = null,
                template = "spawn-point",
                x = 0f,
                y = 0f,
                width = 0.25f,
                height = 0.25f,
                state = null,
                isRigid = false,
                speed = 2.8f,
                moveDirection = "UP_RIGHT"
        ), obj)
    }
}