package com.mirage.connection

import com.mirage.gamelogic.GameLogic
import com.mirage.gamelogic.GameLogicImpl
import com.mirage.utils.gameobjects.GameObjects
import com.mirage.utils.messaging.MoveDirection
import com.mirage.utils.messaging.ServerMessage
import com.mirage.utils.messaging.StateDifferenceMessage
import rx.Observable

/**
 * Реализация интерфейса Connection, работающая с локальным сервером (одиночная игра)
 */
class LocalConnection(mapName: String) : Connection {

    private val logic : GameLogic = GameLogicImpl(mapName)

    override val observable: Observable<ServerMessage> = logic.observable.map { StateDifferenceMessage(it.stateDifference) }

    private var playerID: Long? = null

    override fun getPlayerID(): Long? = playerID

    override var bufferedMoveDirection : MoveDirection? = null
    override var bufferedMoving : Boolean? = null

    //TODO Отправка сообщений о движении
    override fun setMoveDirection(md: MoveDirection) {
        bufferedMoveDirection = md
    }

    override fun setMoving(isMoving: Boolean) {
        bufferedMoving = isMoving
    }

    override fun startMoving(md: MoveDirection) {
        setMoveDirection(md)
        setMoving(true)
    }

    override fun stopMoving() {
        setMoving(false)
    }

    /**
     * Стартует логику и блокирует поток, пока не будет получено первое состояние, затем возвращает это состояние.
     * Следует подписываться на [observable] до вызова [startGame]
     */
    fun startGame() {
        //TODO Разобраться с многопоточностью
        //TODO Разобраться с получением начального состояния
        playerID = logic.addNewPlayer()
        val obs = logic.observable.first()
        logic.startLogic()
        return obs.toBlocking().first().originState
    }

    override fun close() = logic.dispose()

}