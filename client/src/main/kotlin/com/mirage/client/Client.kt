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
import com.mirage.utils.virtualscreen.VirtualScreen
import com.mirage.utils.virtualscreen.VirtualScreenGdxImpl

object Client : ApplicationListener {

    private var currentScreen : Screen? = null
    private val virtualScreen : VirtualScreen = VirtualScreenGdxImpl()
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
            }
        }
        Gdx.input.inputProcessor = mainMenuScreen.inputProcessor
        currentScreen = mainMenuScreen
    }

    @Synchronized
    private fun startSinglePlayerGame(mapName: String) {
        connection = LocalConnection(mapName).also {
            it.start()
            val map = SceneLoader.loadScene(mapName).first
            val gameScreen = GameScreen(map)
            virtualScreen.setTileSet(map.tileSetName)
            it.serverMessages.subscribe { msg ->
                gameScreen.handleServerMessage(msg)
            }
            gameScreen.inputMessages.subscribe { msg ->
                when (msg) {
                    is ChangeSceneClientMessage -> {

                    }
                    else -> it.sendMessage(msg)
                }
            }
            Gdx.input.inputProcessor = gameScreen.inputProcessor
            currentScreen = gameScreen
        }
    }

    override fun create() {
        //TODO Загрузка профиля
        openMainMenu()
    }

    override fun pause() {}
    override fun resume() {}
    override fun dispose() {}

    override fun render() {
        val screen = virtualScreen
        screen.begin()
        gl.glClearColor(0.25f, 0.25f, 0.25f, 1f)
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        currentScreen?.render(screen, System.currentTimeMillis())
        screen.end()
    }

    override fun resize(width: Int, height: Int) {
        virtualScreen.resize(width, height)
    }

}