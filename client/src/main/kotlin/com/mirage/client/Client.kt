package com.mirage.client

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.graphics.GL20
import com.mirage.connection.Connection
import com.mirage.connection.LocalConnection
import com.mirage.ui.Screen
import com.mirage.ui.game.GameScreen
import com.mirage.ui.mainmenu.MainMenuScreen
import com.mirage.utils.game.maps.SceneLoader
import com.mirage.utils.messaging.ChangeSceneClientMessage
import com.mirage.utils.messaging.ExitClientMessage
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

    @Synchronized
    private fun openMainMenu() {
        val mainMenuScreen = MainMenuScreen(virtualScreen)
        mainMenuScreen.inputMessages.subscribe { msg ->
            when (msg) {
                is ChangeSceneClientMessage -> {
                    when (msg.newScene) {
                        ChangeSceneClientMessage.Scene.SINGLEPLAYER_GAME -> {
                            //TODO
                            startSinglePlayerGame("test")
                        }
                    }
                }
                is ExitClientMessage -> {
                    exitProcess(msg.exitCode)
                }
            }
        }
        Gdx.input.inputProcessor = mainMenuScreen.inputProcessor
        currentScreen = mainMenuScreen
    }

    @Synchronized
    private fun startSinglePlayerGame(mapName: String) {
        val map = SceneLoader.loadMap(mapName)
        val gameScreen = GameScreen(map, virtualScreen)
        virtualScreen.setTileSet(map.tileSetName)
        val connection : Connection = LocalConnection(mapName) {
            gameScreen.handleServerMessage(it)
        }
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
                else -> connection.sendMessage(msg)
            }
        }
        connection.start()
        this.connection = connection
        currentScreen = gameScreen
        Gdx.input.inputProcessor = gameScreen.inputProcessor
    }


    override fun create() {
        //TODO Загрузка профиля
        openMainMenu()
    }

    override fun pause() {}
    override fun resume() {}
    override fun dispose() {}

    override fun render() {
        gl.glClearColor(0.25f, 0.25f, 0.25f, 1f)
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

}