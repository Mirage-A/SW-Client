package com.mirage.ui.game

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Состояние интерфейса.
 * Изменяется классами [GameInputProcessor] (при вводе пользователя) и [GameScreen] (при получении данных от логики).
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