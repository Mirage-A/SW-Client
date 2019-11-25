package com.mirage.ui.game

import com.mirage.utils.game.objects.GameObject
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * Состояние интерфейса.
 * Изменяется классами [DesktopGameInputProcessor] (при вводе пользователя) и [GameScreen] (при получении данных от логики).
 * Для рендеринга создаётся копия, которая далее не изменяется.
 * Этот объект и его состояние должно быть защищено блокировкой this
 */
class GameUIState(
        var bufferedMoving : Boolean? = null,
        var bufferedMoveDirection : GameObject.MoveDirection? = null,
        var lastSentMoving : Boolean? = null,
        var lastSentMoveDirection : GameObject.MoveDirection? = null
) {

    val lock: Lock = ReentrantLock(true)

    /**
     * Создаёт копию состояния.
     * Изменение изначального состояния не должно никак влиять на копию.
     */
    fun copy() : GameUIState = GameUIState(
            bufferedMoving, bufferedMoveDirection, lastSentMoving, lastSentMoveDirection
    )

    override fun toString(): String {
        return "$lastSentMoving $bufferedMoving $lastSentMoveDirection $lastSentMoveDirection"
    }

}