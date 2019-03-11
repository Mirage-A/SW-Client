package com.mirage.client

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.mirage.utils.Assets
import com.mirage.connection.Connection
import com.mirage.connection.LocalConnection
import com.mirage.utils.*
import com.mirage.utils.GameState
import com.mirage.utils.extensions.position
import com.mirage.view.screens.GameScreen


object Client : Game(), InputProcessor {

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
    private const val EPS_TIME = 50L

    val state = GameState()

    var connection : Connection? = null

    fun messageListener(msg: UpdateMessage) {
        when (msg) {
            is MapChangeMessage -> {
                Log.i("MapChangeMessage received: $msg")
                state.objects.clear()
                state.map = TmxMapLoader().load("${Assets.assetsPath}maps/${msg.mapName}.tmx")
                for (layer in state.map.layers) {
                    while (layer.objects.count != 0) {
                        layer.objects.remove(0)
                    }
                }
            }
            is NewObjectMessage -> {
                Log.i("NewObjectMessage received: ${msg.obj.name}")
                state.objects[msg.id] = msg.obj
                (screen as? GameScreen)?.drawers?.addObjectDrawer(msg.obj)
            }
            is MoveObjectMessage -> {
                state.objects[msg.id]?.position = msg.newPosition
            }
        }
    }

    override fun render() {
        connection?.checkNewMessages()
        state.playerID = connection?.getPlayerID()
        super.render() // Перерисовка view
    }

    override fun create() {
        Gdx.input.inputProcessor = this
        val gameScreen = GameScreen(state)
        connection = LocalConnection().apply {
            startGame()
            gameScreen.updateResources()
            startLogic()
            addMessageListener(::messageListener)
        }
        setScreen(gameScreen)
    }

    private fun writeFile(fileName: String, text: String) {
        val file = Gdx.files.local("$fileName.txt")
        file.writeString(text, false)
    }

    override fun keyDown(keycode: Int): Boolean {
        connection?.apply {
            when (keycode) {
                Input.Keys.W -> {
                    if (state.isPlayerMoving()) {
                        when (state.getPlayerMoveDirection()) {
                            MoveDirection.LEFT -> {
                                startMoving(MoveDirection.UP_LEFT)
                            }
                            MoveDirection.RIGHT -> {
                                startMoving(MoveDirection.UP_RIGHT)
                            }
                            else -> {
                                startMoving(MoveDirection.UP)
                            }
                        }
                    } else {
                        startMoving(MoveDirection.UP)
                    }
                }
                Input.Keys.A -> {
                    if (state.isPlayerMoving()) {
                        when (state.getPlayerMoveDirection()) {
                            MoveDirection.UP -> {
                                startMoving(MoveDirection.UP_LEFT)
                            }
                            MoveDirection.DOWN -> {
                                startMoving(MoveDirection.DOWN_LEFT)
                            }
                            else -> {
                                startMoving(MoveDirection.LEFT)
                            }
                        }
                    } else {
                        startMoving(MoveDirection.LEFT)
                    }
                }
                Input.Keys.S -> {
                    if (state.isPlayerMoving()) {
                        when (state.getPlayerMoveDirection()) {
                            MoveDirection.LEFT -> {
                                startMoving(MoveDirection.DOWN_LEFT)
                            }
                            MoveDirection.RIGHT -> {
                                startMoving(MoveDirection.DOWN_RIGHT)
                            }
                            else -> {
                                startMoving(MoveDirection.DOWN)
                            }
                        }
                    } else {
                        startMoving(MoveDirection.DOWN)
                    }
                }
                Input.Keys.D -> {
                    if (state.isPlayerMoving()) {
                        when (state.getPlayerMoveDirection()) {
                            MoveDirection.UP -> {
                                startMoving(MoveDirection.UP_RIGHT)
                            }
                            MoveDirection.DOWN -> {
                                startMoving(MoveDirection.DOWN_RIGHT)
                            }
                            else -> {
                                startMoving(MoveDirection.RIGHT)
                            }
                        }
                    } else {
                        startMoving(MoveDirection.RIGHT)
                    }
                }
                Input.Keys.ESCAPE -> {
                    //TODO Выход из игры
                    Gdx.app.exit()
                }
            }
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        connection?.apply {
            when (keycode) {
                Input.Keys.W -> {
                    wReleasedTime = System.currentTimeMillis()
                    if (state.isPlayerMoving()) {
                        when (state.getPlayerMoveDirection()) {
                            MoveDirection.UP_LEFT -> {
                                startMoving(MoveDirection.LEFT)
                            }
                            MoveDirection.UP_RIGHT -> {
                                startMoving(MoveDirection.RIGHT)
                            }
                            MoveDirection.UP -> {
                                if (aReleasedTime >= dReleasedTime && wReleasedTime - aReleasedTime < EPS_TIME) {
                                    setMoveDirection(MoveDirection.UP_LEFT)
                                } else if (dReleasedTime >= aReleasedTime && wReleasedTime - dReleasedTime < EPS_TIME) {
                                    setMoveDirection(MoveDirection.UP_RIGHT)
                                }
                                stopMoving()
                            }
                            else -> {
                            }
                        }
                    }
                }
                Input.Keys.A -> {
                    aReleasedTime = System.currentTimeMillis()
                    if (state.isPlayerMoving()) {
                        when (state.getPlayerMoveDirection()) {
                            MoveDirection.UP_LEFT -> {
                                startMoving(MoveDirection.UP)
                            }
                            MoveDirection.DOWN_LEFT -> {
                                startMoving(MoveDirection.DOWN)
                            }
                            MoveDirection.LEFT -> {
                                if (wReleasedTime >= sReleasedTime && aReleasedTime - wReleasedTime < EPS_TIME) {
                                    setMoveDirection(MoveDirection.UP_LEFT)
                                } else if (sReleasedTime >= wReleasedTime && aReleasedTime - sReleasedTime < EPS_TIME) {
                                    setMoveDirection(MoveDirection.DOWN_LEFT)
                                }
                                stopMoving()
                            }
                            else -> {
                            }
                        }
                    }
                }
                Input.Keys.S -> {
                    sReleasedTime = System.currentTimeMillis()
                    if (state.isPlayerMoving()) {
                        when (state.getPlayerMoveDirection()) {
                            MoveDirection.DOWN_LEFT -> {
                                startMoving(MoveDirection.LEFT)
                            }
                            MoveDirection.DOWN_RIGHT -> {
                                startMoving(MoveDirection.RIGHT)
                            }
                            MoveDirection.DOWN -> {
                                if (aReleasedTime >= dReleasedTime && sReleasedTime - aReleasedTime < EPS_TIME) {
                                    setMoveDirection(MoveDirection.DOWN_LEFT)
                                } else if (dReleasedTime >= aReleasedTime && sReleasedTime - dReleasedTime < EPS_TIME) {
                                    setMoveDirection(MoveDirection.DOWN_RIGHT)
                                }
                                stopMoving()
                            }
                            else -> {
                            }
                        }
                    }
                }
                Input.Keys.D -> {
                    dReleasedTime = System.currentTimeMillis()
                    if (state.isPlayerMoving()) {
                        when (state.getPlayerMoveDirection()) {
                            MoveDirection.UP_RIGHT -> {
                                startMoving(MoveDirection.UP)
                            }
                            MoveDirection.DOWN_RIGHT -> {
                                startMoving(MoveDirection.DOWN)
                            }
                            MoveDirection.RIGHT -> {
                                if (wReleasedTime >= sReleasedTime && dReleasedTime - wReleasedTime < EPS_TIME) {
                                    setMoveDirection(MoveDirection.UP_RIGHT)
                                } else if (sReleasedTime >= wReleasedTime && dReleasedTime - sReleasedTime < EPS_TIME) {
                                    setMoveDirection(MoveDirection.DOWN_RIGHT)
                                }
                                stopMoving()
                            }
                            else -> {
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    private var mdBtnPressed = false
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val x = screenX * GameScreen.DEFAULT_SCREEN_WIDTH / Gdx.graphics.width
        val y = GameScreen.DEFAULT_SCREEN_HEIGHT - screenY * GameScreen.DEFAULT_SCREEN_HEIGHT / Gdx.graphics.height
        when (config["platform"]) {
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
            connection?.stopMoving()
            return true
        }
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val x = screenX * GameScreen.DEFAULT_SCREEN_WIDTH / Gdx.graphics.width
        val y = GameScreen.DEFAULT_SCREEN_HEIGHT - screenY * GameScreen.DEFAULT_SCREEN_HEIGHT / Gdx.graphics.height
        when (config["platform"]) {
            "android" -> {
                if (handleAndroidMoving(x, y)) return true
            }
            else -> {}
        }
        return false
    }

    private fun handleAndroidMoving(x: Float, y: Float) : Boolean {
        val range = Math.sqrt(Math.pow(GameScreen.mdAreaCenterX - x.toDouble(), 2.0) + Math.pow(GameScreen.mdAreaCenterX - y.toDouble(), 2.0))
        if (range < GameScreen.mdAreaRadius) {
            if (range < GameScreen.mdAreaRadius / 2) {
                mdBtnPressed = false
                connection?.stopMoving()
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
                    Math.atan(deltaY / deltaX.toDouble()) + Math.PI
                }
                deltaX > 0 -> {
                    Math.atan(deltaY / deltaX.toDouble())
                }
                deltaY < 0 -> {
                    Math.PI / 2
                }
                else -> {
                    -Math.PI / 2
                }
            }
            connection?.startMoving(MoveDirection.fromMoveAngle((angle + Math.PI / 4).toFloat()))
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
}