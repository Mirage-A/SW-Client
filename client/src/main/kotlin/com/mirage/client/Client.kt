package com.mirage.client

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.mirage.connection.Connection
import com.mirage.connection.LocalConnection
import com.mirage.ui.Screen
import com.mirage.ui.game.GameScreen
import com.mirage.ui.mainmenu.MainMenuScreen
import com.mirage.ui.newgame.NewGameScreen
import com.mirage.utils.INTERPOLATION_DELAY_MILLIS
import com.mirage.utils.PLATFORM
import com.mirage.utils.game.maps.SceneLoader
import com.mirage.utils.messaging.ChangeSceneClientMessage
import com.mirage.utils.messaging.ClearTargetMessage
import com.mirage.utils.messaging.ExitClientMessage
import com.mirage.utils.messaging.NewTargetMessage
import com.mirage.utils.preferences.Prefs
import com.mirage.utils.preferences.Profile
import com.mirage.utils.virtualscreen.VirtualScreen
import com.mirage.utils.virtualscreen.VirtualScreenGdxImpl
import kotlin.system.exitProcess

object Client : ApplicationListener {

    private val virtualScreen : VirtualScreen = VirtualScreenGdxImpl()
    private var currentScreen : Screen? = null
        private set(value) {
            value?.resize(virtualScreen.width, virtualScreen.height)
            field = value
        }
    private var connection : Connection? = null

    private fun setInputProcessor(screen: Screen) {
        Gdx.input.inputProcessor = object : InputProcessor {
            override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                virtualScreen.stage.touchUp(screenX, screenY, pointer, button)
                screen.inputProcessor.touchUp(screenX, screenY, pointer, button)
                return true
            }

            override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
                virtualScreen.stage.mouseMoved(screenX, screenY)
                screen.inputProcessor.mouseMoved(screenX, screenY)
                return true
            }

            override fun keyTyped(character: Char): Boolean {
                virtualScreen.stage.keyTyped(character)
                screen.inputProcessor.keyTyped(character)
                return true
            }

            override fun scrolled(amount: Int): Boolean {
                virtualScreen.stage.scrolled(amount)
                screen.inputProcessor.scrolled(amount)
                return true
            }

            override fun keyUp(keycode: Int): Boolean {
                virtualScreen.stage.keyUp(keycode)
                screen.inputProcessor.keyUp(keycode)
                return true
            }

            override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
                virtualScreen.stage.touchDragged(screenX, screenY, pointer)
                screen.inputProcessor.touchDragged(screenX, screenY, pointer)
                return true
            }

            override fun keyDown(keycode: Int): Boolean {
                virtualScreen.stage.keyDown(keycode)
                screen.inputProcessor.keyDown(keycode)
                return true
            }

            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                virtualScreen.stage.touchDown(screenX, screenY, pointer, button)
                screen.inputProcessor.touchDown(screenX, screenY, pointer, button)
                return true
            }
        }
    }

    private fun openMainMenu() {
        virtualScreen.stage.clear()
        val mainMenuScreen = MainMenuScreen(virtualScreen)
        mainMenuScreen.inputMessages.subscribe { msg ->
            when (msg) {
                is ChangeSceneClientMessage -> {
                    when (msg.newScene) {
                        ChangeSceneClientMessage.Scene.SINGLEPLAYER_GAME -> {
                            //TODO
                            startSinglePlayerGame(Prefs.profile.currentMap)
                        }
                        ChangeSceneClientMessage.Scene.MULTIPLAYER_LOBBY -> {

                        }
                        ChangeSceneClientMessage.Scene.SETTINGS_MENU -> {
                            val fullScreen = Prefs.settings.desktopFullScreen.get()
                            if (fullScreen) setDesktopWindowedMode()
                            else setDesktopFullScreen()
                        }
                        ChangeSceneClientMessage.Scene.NEW_PROFILE_MENU -> {
                            startNewGame()
                        }
                    }
                }
                is ExitClientMessage -> {
                    Prefs.savePreferences()
                    exitProcess(msg.exitCode)
                }
            }
        }
        setInputProcessor(mainMenuScreen)
        currentScreen = mainMenuScreen
    }

    private fun startNewGame() {
        virtualScreen.stage.clear()
        val newGameScreen = NewGameScreen(virtualScreen)
        newGameScreen.inputMessages.subscribe { msg ->
            when (msg) {
                is ChangeSceneClientMessage -> {
                    when (msg.newScene) {
                        ChangeSceneClientMessage.Scene.MAIN_MENU -> openMainMenu()
                        ChangeSceneClientMessage.Scene.SINGLEPLAYER_GAME -> startSinglePlayerGame(Prefs.profile.currentMap)
                    }
                }
            }
        }
        setInputProcessor(newGameScreen)
        currentScreen = newGameScreen
    }

    private fun startSinglePlayerGame(mapName: String) {
        virtualScreen.stage.clear()
        val map = SceneLoader.loadMap(mapName)
        val gameScreen = GameScreen(map, virtualScreen)
        virtualScreen.setTileSet(map.tileSetName)
        val connection : Connection = LocalConnection(mapName)
        gameScreen.inputMessages.subscribe { msg ->
            when (msg) {
                is ChangeSceneClientMessage -> {
                    when (msg.newScene) {
                        ChangeSceneClientMessage.Scene.MAIN_MENU -> {
                            connection.close()
                            openMainMenu()
                        }
                    }
                }
                is NewTargetMessage -> {
                    gameScreen.changeTarget(msg.virtualScreenPoint)
                }
                is ClearTargetMessage -> {
                    gameScreen.clearTarget()
                }
                else -> connection.sendMessage(msg)
            }
        }
        connection.start()
        this.connection = connection
        currentScreen = gameScreen
        setInputProcessor(gameScreen)
        Thread.sleep(INTERPOLATION_DELAY_MILLIS)
    }


    override fun create() {
        //TODO Загрузка профиля
        if (PLATFORM == "desktop" || PLATFORM == "desktop-test") {
            val fullScreen = Prefs.settings.desktopFullScreen.get()
            if (fullScreen) setDesktopFullScreen()
            else setDesktopWindowedMode()
        }
        openMainMenu()
    }

    override fun pause() {
        Prefs.savePreferences()
    }

    override fun resume() {}

    override fun dispose() {
        Prefs.savePreferences()
    }

    override fun render() {
        connection?.forNewMessages(maximumProcessingTime = 15L) {
            (currentScreen as? GameScreen)?.handleServerMessage(it)
        }
        gl.glClearColor(0f, 0f, 0f, 1f)
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        val screen = virtualScreen
        screen.begin()
        currentScreen?.render(screen, System.currentTimeMillis())
        screen.end()
    }

    override fun resize(width: Int, height: Int) {
        virtualScreen.resize(width, height)
        currentScreen?.resize(virtualScreen.width, virtualScreen.height)
    }

    private fun setDesktopFullScreen() {
        Prefs.settings.desktopFullScreen.set(true)
        Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
    }

    private fun setDesktopWindowedMode() {
        Prefs.settings.desktopFullScreen.set(false)
        Gdx.graphics.setWindowedMode(800, 600)
    }

}