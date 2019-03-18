package com.mirage.server

import com.mirage.gamelogic.LogicFacade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.coroutines.CoroutineContext

class Room {

    private val logic = LogicFacade()

    private val players : MutableList<Player> = LinkedList()

    fun getPlayerByIndex(index: Int): Player? =
            try { players[index] } catch(ex: Exception) { null }

    fun getPlayersCount() = players.size

}