package com.mirage.server

import com.mirage.logic.GameLogic
import com.mirage.logic.GameLogicImpl
import com.mirage.core.extensions.PlayerCreationRequest
import com.mirage.core.extensions.QuestProgress
import com.mirage.core.game.objects.properties.Equipment
import java.util.*

/**
 * Комната с игроками на одной карте
 * Содержит поток логики и список игроков, находящихся в этой комнате
 * @see Player
 */
class Room {

    private lateinit var logic: GameLogic

    private val players : MutableList<Player> = Collections.synchronizedList(ArrayList())


    /**
     * Освобождает все ресурсы, подготавливает комнату к удалению, возвращает список игроков
     */
    fun close() : MutableList<Player> {
        logic.pauseLogic()
        logic.stopLogic()
        return players
        //TODO
    }

    fun disconnectPlayer(player: Player) {
        //TODO Обработка отключения игрока
        players.remove(player)
    }

    fun getPlayerByIndex(index: Int): Player? =
            try { players[index] } catch(ex: Exception) { null }

    fun getPlayersCount() = players.size

    fun start() {
        //TODO выбор карты, подписка на логику
        logic = GameLogicImpl("test-server")
        logic.startLogic()
    }

    fun addPlayer(p: Player) {
        players.add(p)
        p.room = this
        val request = PlayerCreationRequest("", QuestProgress(), Equipment()) { p.id = it }
        logic.addNewPlayer(request)
    }
}