package com.mirage.gamelogic

import com.mirage.utils.game.maps.SceneLoader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

internal class GameLogicImplTest{

    @Test
    fun testStart() {
        val logic = GameLogicImpl("micro-test")
        logic.startLogic()
        logic.stopLogic()
        val state = logic.latestState.asObservable().toBlocking().first()
        assertEquals(SceneLoader.loadScene("micro-test").second, state.first)
    }

    @Test
    fun testNewPlayer() {
        //TODO
        /*
        val logic = GameLogicImpl("micro-test")
        logic.startLogic()
        val id = logic.addNewPlayer()
        logic.stopLogic()
        val state = logic.latestState.asObservable().toBlocking().first()
        assertEquals(Long.MIN_VALUE + 2, id)
        assertEquals(3, state.first.objects.size)*/
    }

    @Test
    @RepeatedTest(10)
    fun testMinorStateUpdate() {
        //TODO Этот тест иногда ложится, нужно разобраться с потокобезопасностью
        /*
        val logic = GameLogicImpl("moving-micro-test")
        logic.startLogic()
        Thread.sleep(5L)
        val firstState = logic.latestState.asObservable().toBlocking().first()
        Thread.sleep(25L)
        val messages = ArrayList<ServerMessage>()
        logic.serverMessages.asObservable().subscribe {
            messages.add(it)
        }
        Thread.sleep(25L)
        logic.stopLogic()
        Thread.sleep(100L)
        val secondState = logic.latestState.asObservable().toBlocking().first()
        assert(Point(0.5f, 0.5f) near firstState.first[Long.MIN_VALUE]!!.position)
        assert(secondState.first[Long.MIN_VALUE]!!.position.x in 1.4f..1.6f)
        assert(secondState.first[Long.MIN_VALUE]!!.position.y in 0.4f..0.6f)

        println(messages)
        assertEquals(2, messages.size)
        val firstDiff = (messages[0] as GameStateUpdateMessage).diff
        //TODO assertEquals(StateDifference(hashMapOf(), treeSetOf(), hashMapOf()), firstDiff)
        val secondDiff = (messages[1] as GameStateUpdateMessage).diff
        assertEquals(secondState.first, secondDiff.projectOn(firstState.first))*/
    }
}