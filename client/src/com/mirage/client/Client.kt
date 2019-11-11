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
import com.mirage.utils.messaging.InitialGameStateMessage
import com.mirage.view.utils.calculateViewportSize
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
            Gdx.input.inputProcessor = currentScreen
        }
    }

    override fun create() {
        JsePlatform.standardGlobals()
        batch = SpriteBatch()

        //TODO
        startSinglePlayerGame("micro-test")

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
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
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
/*
    fun messageListener(msg: ServerMessage) {
        when (msg) {
            is InitialGameStateMessage -> {
                //TODO Разобраться со сменой карты, пересозданием контроллеров/экранов
                Log.i("MapChangeMessage received: $msg")
                val gameController = (controller as? GameController) ?: return
                gameController.state.map = SceneLoader.loadScene(msg.mapName).first
                for (layer in gameController.state.map.layers) {
                    while (layer.objects.count != 0) {
                        layer.objects.remove(0)
                    }
                }
                setScreen(gameController.screen)
            }
            is GameStateUpdateMessage -> {
                Log.i("StateDifferenceMessage received: $msg")
            }
            is ReturnCodeMessage -> {
                Log.i("ReturnCodeMessage received: $msg")
            }
            /*is MapChangeMessage -> {
                Log.i("MapChangeMessage received: $msg")
                val gameController = (controller as? GameController) ?: return
                //TODO objects.clear()
                gameController.state.map = TmxMapLoader().load("${Assets.assetsPath}maps/${msg.mapName}.tmx")
                for (layer in gameController.state.map.layers) {
                    while (layer.objects.count != 0) {
                        layer.objects.remove(0)
                    }
                }
                (screen as? GameScreen)?.updateResources()
            }
            is NewObjectMessage -> {
                Log.i("NewObjectMessage received: ${msg.obj.name}")
                val gameController = (controller as? GameController) ?: return
                gameController.state.objects[msg.id] = msg.obj.clone()
                (screen as? GameScreen)?.drawers?.addObjectDrawer(msg.obj)
            }
            is PositionSnapshotMessage -> {
                val gameController = (controller as? GameController) ?: return
                gameController.snapshotManager.addNewSnapshot(msg.snapshot)
                for ((id, obj) in gameController.state.objects) {
                    val oldPos = obj.position
                    val newPos = msg.snapshot.positions[id] ?: oldPos
                    if (oldPos.x != newPos.x || oldPos.y != newPos.y) {
                        obj.position = newPos
                        if (obj.properties.containsKey("on-move-client")) {
                            val table = tableOf("object" to obj, "oldPos" to oldPos, "newPos" to newPos)
                            runClientScript(obj.properties.getString("on-move-client"), table, gameController.actions)
                        }
                        if (obj.properties.containsKey("on-tile-entered-client") &&
                                (oldPos.x.toInt() != newPos.x.toInt() || oldPos.y.toInt() != newPos.y.toInt())) {
                            val table = tableOf("object" to obj, "oldPos" to oldPos, "newPos" to newPos)
                            runClientScript(obj.properties.getString("on-tile-entered-client"), table, gameController.actions)
                        }
                    }
                }
            }*/
        }
    }
*/

}