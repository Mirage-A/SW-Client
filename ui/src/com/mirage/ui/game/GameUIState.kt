package com.mirage.ui.game

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * Состояние интерфейса.
 * Изменяется классами [DesktopGameInputProcessor] (при вводе пользователя) и [GameScreen] (при получении данных от логики).
 * Для рендеринга создаётся копия, которая далее не изменяется.
 */
class GameUIState {

    val lock: Lock = ReentrantLock(true)

    /**
     * Создаёт копию состояния.
     * Изменение изначального состояния не должно никак влиять на копию.
     */
    fun copy() : GameUIState = GameUIState(

    )

}