package com.mirage.client

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.graphics.GL20
import com.mirage.connection.Connection
import com.mirage.connection.LocalConnection
import com.mirage.ui.Screen
import com.mirage.ui.game.GameScreen
import com.mirage.utils.game.maps.SceneLoader
import com.mirage.utils.virtualscreen.VirtualScreen
import com.mirage.utils.virtualscreen.VirtualScreenGdxImpl

object Client : ApplicationListener {

    private var currentScreen : Screen? = null
    private val virtualScreen : Lazy<VirtualScreen> = lazy(LazyThreadSafetyMode.NONE) { VirtualScreenGdxImpl() }
    private var connection : Connection? = null

    private fun startSinglePlayerGame(mapName: String) {
        connection = LocalConnection(mapName)
        connection!!.let {
            it.start()
            val map = SceneLoader.loadScene(mapName).first
            currentScreen = GameScreen(map)
            virtualScreen.value.setTileSet(map.tileSetName)
            it.serverMessages.subscribe { msg ->
                currentScreen?.handleServerMessage(msg)
            }
            currentScreen?.inputMessages?.subscribe { msg ->
                it.sendMessage(msg)
            }
            Gdx.input.inputProcessor = currentScreen
        }
    }

    override fun create() {
        //TODO
        startSinglePlayerGame("test")
    }

    override fun pause() {}
    override fun resume() {}
    override fun dispose() {}

    override fun render() {
        gl.glClearColor(0.25f, 0.25f, 0.25f, 1f)
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        currentScreen?.render(virtualScreen.value, System.currentTimeMillis())

    }

    override fun resize(width: Int, height: Int) {
        virtualScreen.value.resize(width, height)
    }

}