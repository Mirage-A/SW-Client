package com.mirage.ui.game

import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.virtualscreen.VirtualScreen
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * Состояние интерфейса.
 * Изменяется классами [DesktopGameInputProcessor] (при вводе пользователя) и [GameScreen] (при получении данных от логики).
 * Для рендеринга создаётся копия, которая далее не изменяется.
 * Этот объект и его состояние должно быть защищено блокировкой this
 */
class GameUIState(virtualScreen: VirtualScreen) {

    var bufferedMoving : Boolean? = null
    var bufferedMoveDirection : GameObject.MoveDirection? = null
    var lastSentMoving : Boolean? = null
    var lastSentMoveDirection : GameObject.MoveDirection? = null


    fun resize(virtualWidth: Float, virtualHeight: Float) {

    }

}