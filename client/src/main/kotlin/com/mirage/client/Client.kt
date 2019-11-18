package com.mirage.client

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.connection.Connection
import com.mirage.connection.LocalConnection
import com.mirage.ui.Screen
import com.mirage.ui.game.GameScreen
import com.mirage.utils.game.maps.SceneLoader
import com.mirage.gameview.utils.calculateViewportSize
import org.luaj.vm2.lib.jse.JsePlatform

object Client : ApplicationListener {

    private var currentScreen : Screen? = null
    private var virtualScreenWidth : Int = 0
    private var virtualScreenHeight : Int = 0
    private var batch : SpriteBatch? = null
    private val camera : OrthographicCamera = OrthographicCamera()
    private var connection : Connection? = null

    private fun startSinglePlayerGame(mapName: String) {
        connection = LocalConnection(mapName)
        connection!!.let {
            it.start()
            currentScreen = GameScreen(SceneLoader.loadScene(mapName).first)
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
        JsePlatform.standardGlobals()
        batch = SpriteBatch()

        //TODO
        startSinglePlayerGame("test")

        camera.setToOrtho(false)
        camera.position.x = 0f
        camera.position.y = 0f
        camera.viewportWidth = 800f
        camera.viewportHeight = 600f
        camera.update()
        batch!!.projectionMatrix = camera.combined
    }

    override fun pause() {}
    override fun resume() {}
    override fun dispose() {}

    override fun render() {
        gl.glClearColor(0.25f, 0.25f, 0.25f, 1f)
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch?.let {
            currentScreen?.render(it, virtualScreenWidth, virtualScreenHeight, System.currentTimeMillis())
        }
    }

    override fun resize(width: Int, height: Int) {
        val newVirtualScreenSize = calculateViewportSize(width.toFloat(), height.toFloat())
        virtualScreenWidth = newVirtualScreenSize.width.toInt()
        virtualScreenHeight = newVirtualScreenSize.height.toInt()
        camera.viewportWidth = newVirtualScreenSize.width
        camera.viewportHeight = newVirtualScreenSize.height
        camera.update()
        batch!!.projectionMatrix = camera.combined
    }

}