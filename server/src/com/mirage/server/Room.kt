package com.mirage.server

import com.mirage.gamelogic.LogicFacade
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * Комната с игроками на одной карте
 * Содержит поток логики и предоставляет корутинам соединения с игроками одной комнаты общий контекст.
 */
class Room {


    private val logic = LogicFacade()

    private val players : MutableList<Player> = LinkedList()

    init {
        //TODO Убрать
        GlobalScope.launch {
            while(true) {
                for (pl in players) {
                    pl.checkNewMessages()
                }
                delay(500)
            }
        }
    }

    /**
     * Разрывает все соединения с игроками, освобождает все ресурсы, подготавливает комнату к удалению.
     */
    fun close() {
        logic.pauseLogic()
        logic.stopLogic()
        //TODO
    }

    fun getPlayerByIndex(index: Int): Player? =
            try { players[index] } catch(ex: Exception) { null }

    fun getPlayersCount() = players.size

    fun addPlayer(p: Player) = players.add(p)
}