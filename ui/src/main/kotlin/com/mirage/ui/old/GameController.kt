package com.mirage.ui.old
/*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.mirage.client.Client
import com.mirage.client.ClientScriptActionsImpl
import com.mirage.connection.Connection
import com.mirage.utils.PLATFORM
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.objects.MoveDirection
import com.mirage.utils.game.states.SnapshotManager
import com.mirage.gameview.old.GameScreen
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt

class GameController(val connection: Connection, val map: GameMap) : Controller {

    /**
     * Время отпускания клавиши передвижения
     */
    private var wReleasedTime = 0L
    private var aReleasedTime = 0L
    private var sReleasedTime = 0L
    private var dReleasedTime = 0L
    /**
     * Интервал времени, за который должны быть отпущены две соседние клавиши передвижения,
     * чтобы персонаж остался в диагональном направлении движения
     */
    private val EPS_TIME = 50L

    private val snapshotManager = SnapshotManager()

    override val screen = GameScreen(snapshotManager, connection.getPlayerID() ?: 0)

    /*var connection : Connection = if (ONLINE_MODE) {
        RemoteConnection().apply {
            addMessageListener(Client::messageListener)
        }
    }
    else {
        LocalConnection().apply {
            addMessageListener(Client::messageListener)
            startGame()
            screen.updateResources()
            startLogic()
        }
    }*/

    val actions = ClientScriptActionsImpl(Client)

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    private var mdBtnPressed = false
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val x = screenX * GameScreen.DEFAULT_SCREEN_WIDTH / Gdx.graphics.width
        val y = GameScreen.DEFAULT_SCREEN_HEIGHT - screenY * GameScreen.DEFAULT_SCREEN_HEIGHT / Gdx.graphics.height
        when (PLATFORM) {
            "android" -> {
                if (handleAndroidMoving(x, y)) return true
            }
            else -> {}
        }
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (mdBtnPressed) {
            mdBtnPressed = false
            connection.stopMoving()
            return true
        }
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val x = screenX * GameScreen.DEFAULT_SCREEN_WIDTH / Gdx.graphics.width
        val y = GameScreen.DEFAULT_SCREEN_HEIGHT - screenY * GameScreen.DEFAULT_SCREEN_HEIGHT / Gdx.graphics.height
        when (PLATFORM) {
            "android" -> {
                if (handleAndroidMoving(x, y)) return true
            }
            else -> {}
        }
        return false
    }

    private fun handleAndroidMoving(x: Float, y: Float) : Boolean {
        val range = sqrt((GameScreen.mdAreaCenterX - x.toDouble()).pow(2.0) + (GameScreen.mdAreaCenterX - y.toDouble()).pow(2.0))
        if (range < GameScreen.mdAreaRadius) {
            if (range < GameScreen.mdAreaRadius / 2) {
                mdBtnPressed = false
                connection.stopMoving()
                return true
            } else {
                mdBtnPressed = true
            }
        }
        if (mdBtnPressed) {
            val deltaX = x - GameScreen.mdAreaCenterX
            val deltaY = y - GameScreen.mdAreaCenterX
            val angle = when {
                deltaX < 0 -> {
                    atan(deltaY / deltaX.toDouble()) + Math.PI
                }
                deltaX > 0 -> {
                    atan(deltaY / deltaX.toDouble())
                }
                deltaY < 0 -> {
                    Math.PI / 2
                }
                else -> {
                    -Math.PI / 2
                }
            }
            connection.startMoving(MoveDirection.fromMoveAngle((angle + Math.PI / 4).toFloat()))
            return true
        }
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }
    override fun dispose() {
        screen.dispose()
    }
}*/