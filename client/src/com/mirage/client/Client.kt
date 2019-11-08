package com.mirage.client

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.mirage.client.controllers.Controller
import com.mirage.client.controllers.EmptyController
import com.mirage.client.controllers.GameController
import com.mirage.client.controllers.MainMenuController
import com.mirage.connection.Connection
import com.mirage.utils.*
import com.mirage.utils.game.maps.SceneLoader
import com.mirage.utils.messaging.*
import org.luaj.vm2.lib.jse.JsePlatform


object Client : Game() {

    /**
     * Текущий [Controller]
     */
    var controller : Controller = EmptyController()

    /**
     * Метод вызывает [dispose] у текущего контроллера, изменяет контроллер на новый и обновляет экран
     * @see Controller.dispose
     */
    fun changeController(newController: Controller) {
        val oldController = controller
        controller = newController
        Gdx.input.inputProcessor = newController
        setScreen(newController.screen)
        oldController.dispose()
    }

    /**
     * Обработчик сообщений [ServerMessage], полученных от логики через [Connection]
     * @see ServerMessage
     * @see Connection
     */
    fun messageListener(msg: ServerMessage) {
        when (msg) {
            is MapChangeMessage -> {
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
            is StateDifferenceMessage -> {
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

    override fun render() {
        (controller as? GameController)?.apply {
            playerID = connection.getPlayerID()
        }
        super.render()
    }

    override fun create() {
        JsePlatform.standardGlobals()
        controller = MainMenuController()
        Gdx.input.inputProcessor = controller
        setScreen(controller.screen)
    }

}