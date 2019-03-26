package com.mirage.client

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.mirage.client.controllers.Controller
import com.mirage.client.controllers.EmptyController
import com.mirage.client.controllers.GameController
import com.mirage.client.controllers.MainMenuController
import com.mirage.utils.Assets
import com.mirage.connection.Connection
import com.mirage.connection.LocalConnection
import com.mirage.connection.RemoteConnection
import com.mirage.scriptrunner.runClientScript
import com.mirage.utils.*
import com.mirage.utils.extensions.*
import com.mirage.utils.messaging.*
import com.mirage.view.screens.GameScreen
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
     * Обработчик сообщений [UpdateMessage], полученных от логики через [Connection]
     * @see UpdateMessage
     * @see Connection
     */
    fun messageListener(msg: UpdateMessage) {
        when (msg) {
            is MapChangeMessage -> {
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
            }
        }
    }

    /**
     * Проверять ли наличие новых сообщений перед рендерингом
     * Обработку сообщений имеет смысл отключать в начале загрузки ресурсов
     */
    var checkMessagesOnRender : Boolean = false

    override fun render() {
        if (checkMessagesOnRender) {
            (controller as? GameController)?.apply {
                connection.checkNewMessages()
                state.playerID = connection.getPlayerID()
            }
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