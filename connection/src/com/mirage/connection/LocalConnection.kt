package com.mirage.connection

import com.mirage.gamelogic.GameLogic
import com.mirage.gamelogic.GameLogicImpl
import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.messaging.MapChangeMessage
import com.mirage.utils.game.objects.MoveDirection
import com.mirage.utils.messaging.ServerMessage
import com.mirage.utils.messaging.StateDifferenceMessage
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Реализация интерфейса Connection, работающая с локальным сервером (одиночная игра)
 */
class LocalConnection(private val mapName: String) : Connection {

    private val logic : GameLogic = GameLogicImpl(mapName)

    /**
     * Приватный Subject, который повторяет сообщения от логики, но при этом позволяет добавлять новые сообщения.
     */
    private val messageRepeater = PublishSubject.create<ServerMessage>()
    /**
     * [Observable], из которого можно получать сообщения от логики.
     * На него следует подписаться до запуска логики.
     * @see startGame
     */
    override val observable: Observable<ServerMessage> = messageRepeater

    private var playerID: Long? = null

    override fun getPlayerID(): Long? = playerID

    override var bufferedMoveDirection : GameObject.MoveDirection? = null
    override var bufferedMoving : Boolean? = null

    //TODO Отправка сообщений о движении
    override fun setMoveDirection(md: GameObject.MoveDirection) {
        bufferedMoveDirection = md
    }

    override fun setMoving(isMoving: Boolean) {
        bufferedMoving = isMoving
    }

    override fun startMoving(md: GameObject.MoveDirection) {
        setMoveDirection(md)
        setMoving(true)
    }

    override fun stopMoving() {
        setMoving(false)
    }

    /**
     * Стартует логику в отдельном потоке.
     * Следует подписываться на [observable] до вызова [startGame]
     */
    fun startGame() {
        var gotInitialState = false
        logic.observable.subscribe {
            println("got msg from logic in connection! $it")
            if (!gotInitialState) {
                gotInitialState = true
                messageRepeater.onNext(MapChangeMessage(mapName, it.finalState, it.createdTimeMillis))
            }
            else {
                messageRepeater.onNext(StateDifferenceMessage(it.stateDifference, it.createdTimeMillis))
            }
        }
        logic.startLogic()
        playerID = logic.addNewPlayer()
    }

    override fun close() = logic.dispose()

}