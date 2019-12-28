package com.mirage.client

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.graphics.GL20
import com.mirage.connection.Connection
import com.mirage.connection.LocalConnection
import com.mirage.core.messaging.ChangeSceneClientMessage
import com.mirage.core.messaging.CloseConnectionMessage
import com.mirage.core.messaging.ExitClientMessage
import com.mirage.core.preferences.Prefs
import com.mirage.core.utils.INTERPOLATION_DELAY_MILLIS
import com.mirage.core.utils.PLATFORM
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.core.virtualscreen.VirtualScreenGdxImpl
import com.mirage.ui.screens.Screen
import com.mirage.ui.screens.game.GameScreen
import com.mirage.ui.screens.loading.LoadingScreen
import com.mirage.ui.screens.mainmenu.MainMenuScreen
import com.mirage.ui.screens.newgame.NewGameScreen
import kotlin.system.exitProcess

object Client : ApplicationListener {

    private val virtualScreen: VirtualScreen = VirtualScreenGdxImpl()
    private var currentScreen: Screen? = null
        private set(value) {
            value?.resize(virtualScreen.width, virtualScreen.height)
            field = value
        }
    private var connection: Connection? = null

    private fun openLoadingScreen() {
        val loadingScreen = LoadingScreen(virtualScreen) { msg ->
            when (msg) {
                is ChangeSceneClientMessage -> {
                    when (msg.newScene) {
                        ChangeSceneClientMessage.Scene.SINGLEPLAYER_GAME -> {
                            startSinglePlayerGame(Prefs.profile.currentMap)
                        }
                        ChangeSceneClientMessage.Scene.MAIN_MENU -> {
                            openMainMenu()
                        }
                    }
                }
            }
        }
        Gdx.input.inputProcessor = loadingScreen
        currentScreen = loadingScreen
    }

    private fun openMainMenu() {
        val mainMenuScreen = MainMenuScreen(virtualScreen) { msg ->
            when (msg) {
                is ChangeSceneClientMessage -> {
                    when (msg.newScene) {
                        ChangeSceneClientMessage.Scene.SINGLEPLAYER_GAME -> {
                            openLoadingScreen()
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
        Gdx.input.inputProcessor = mainMenuScreen
        currentScreen = mainMenuScreen
    }

    private fun startNewGame() {
        val newGameScreen = NewGameScreen(virtualScreen) { msg ->
            when (msg) {
                is ChangeSceneClientMessage -> {
                    when (msg.newScene) {
                        ChangeSceneClientMessage.Scene.MAIN_MENU -> openMainMenu()
                        ChangeSceneClientMessage.Scene.SINGLEPLAYER_GAME -> startSinglePlayerGame(Prefs.profile.currentMap)
                    }
                }
            }
        }
        Gdx.input.inputProcessor = newGameScreen
        currentScreen = newGameScreen
    }

    private fun startSinglePlayerGame(mapName: String) {
        val connection: Connection = LocalConnection(mapName)
        val gameScreen = GameScreen(virtualScreen, mapName) { msg ->
            when (msg) {
                is ChangeSceneClientMessage -> {
                    when (msg.newScene) {
                        ChangeSceneClientMessage.Scene.MAIN_MENU -> {
                            connection.close()
                            openMainMenu()
                        }
                        ChangeSceneClientMessage.Scene.LOADING_SCREEN -> {
                            connection.close()
                            openLoadingScreen()
                        }
                    }
                }
                is CloseConnectionMessage -> {
                    connection.close()
                    this.connection = null
                }
                else -> connection.sendMessage(msg)
            }
        }
        connection.start()
        this.connection = connection
        currentScreen = gameScreen
        Gdx.input.inputProcessor = gameScreen
        Thread.sleep(INTERPOLATION_DELAY_MILLIS)
    }


    override fun create() {
        //TODO Load profile
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
        virtualScreen.begin()
        currentScreen?.render(virtualScreen)
        virtualScreen.end()
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