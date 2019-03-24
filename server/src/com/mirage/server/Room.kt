package com.mirage.server

import com.mirage.gamelogic.LogicFacade
import com.mirage.utils.Timer
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

    private val players : MutableList<Player> = Collections.synchronizedList(LinkedList())


    //TODO Убрать
    private val TEST = Timer(1000L) {
        try {
            for (pl in players) {
                pl.checkNewMessages()
            }
        }
        catch(ex: Exception) {
            ex.printStackTrace()
        }
    }
    init {
        TEST.start()
    }

    /**
     * Разрывает все соединения с игроками, освобождает все ресурсы, подготавливает комнату к удалению.
     */
    fun close() {
        logic.pauseLogic()
        logic.stopLogic()
        //TODO
    }

    fun disconnectPlayer(player: Player) {
        //TODO Обработка отключения игрока
        players.remove(player)
    }

    fun getPlayerByIndex(index: Int): Player? =
            try { players[index] } catch(ex: Exception) { null }

    fun getPlayersCount() = players.size

    fun addPlayer(p: Player) {
        players.add(p)
        p.room = this
        p.id = logic.addNewPlayer()
    }
}