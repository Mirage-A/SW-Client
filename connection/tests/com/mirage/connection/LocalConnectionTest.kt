package com.mirage.connection

import com.mirage.utils.gameobjects.Entity
import com.mirage.utils.maps.SceneLoader
import com.mirage.utils.messaging.MapChangeMessage
import com.mirage.utils.messaging.ServerMessage
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class LocalConnectionTest {

    @Test
    fun testInitMessaging() {
        val conn = LocalConnection("micro-test")
        val map = SceneLoader.loadScene("micro-test")
        val msgList = Collections.synchronizedList(ArrayList<ServerMessage>())
        val subs = conn.observable.subscribe {
            msgList.add(it)
        }
        conn.startGame()
        Thread.sleep(50L)
        subs.unsubscribe()
        Thread.sleep(5L)
        msgList.forEach(::println)
        val mapMsg = msgList[0] as MapChangeMessage
        val expectedObjs = map.second.update(mapOf(Long.MIN_VALUE + 2 to Entity(
                name = "player",
                template = "player",
                x = 1.5f,
                y = 0.5f,
                width = 0.25f,
                height = 0.25f,
                isRigid = true,
                speed = 2.8f,
                moveDirection = "UP_RIGHT",
                isMoving = false,
                scripts = mapOf("on-tile-entered" to "hello")
        ))) { _, obj -> obj}
        assertEquals(expectedObjs, mapMsg.initialObjects)
    }
}