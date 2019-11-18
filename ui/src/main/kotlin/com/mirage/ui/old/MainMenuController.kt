package com.mirage.ui.old
/*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.mirage.client.Client
import com.mirage.connection.LocalConnection
import com.mirage.connection.RemoteConnection
import com.mirage.utils.messaging.CityJoinClientMessage
import com.mirage.utils.messaging.LoginClientMessage
import com.mirage.utils.messaging.ReturnCodeMessage
import com.mirage.utils.messaging.ServerMessage
import com.mirage.gameview.old.MainMenuScreen

class MainMenuController : Controller {

    /**
     * Начать игру
     *   //TODO выбор режима - онлайн/оффлайн
     *   //TODO Исправить костыль с ожиданием загрузки всех объектов - возможно, добавить новый тип сообщения, которое логика отправляет
     *   //TODO новому игроку после отправки информации об объектах на карте.
     *   //TODO Ну или загружать объекты карты из файла, а не ждать сообщений от логики.
     */
    private fun startOfflineGame() {
        Client.checkMessagesOnRender = false
        val gameController = GameController(LocalConnection().apply {
            addMessageListener(Client::messageListener)
            startGame()
            startLogic()
        })
        //TODO Экран загрузки
        while(!gameController.connection.hasNewMessages()) {
            Thread.sleep(2L)
        }
        Thread.sleep(140L)
        val oldController = Client.controller
        Client.controller = gameController
        Client.screen = gameController.screen
        gameController.connection.checkNewMessages()
        gameController.screen.updateResources()
        Gdx.input.inputProcessor = gameController
        oldController.dispose()
        Client.checkMessagesOnRender = true
    }

    /**
     * Начать процедуру со всякими регистрациями/ввода логина-пароля и подключением к серверу
     * //TODO Процедура регистрации/логина
     */
    private fun startOnlineGame() {
        val login = ""
        val password = ""
        Client.checkMessagesOnRender = false
        val remoteConnection = RemoteConnection()
        val loginAnswerListener : (ServerMessage) -> Unit = {
            if (it is ReturnCodeMessage) {
                when (it.returnCode) {
                    0 -> {//TODO Если успешно ввели логин
                        remoteConnection.sendAndFlush(CityJoinClientMessage(0L))
                        remoteConnection.blockUntilHaveMessages()
                        remoteConnection.readOneMessage()
                    }
                    100 -> {//TODO Если успешно подключились к комнате
                        remoteConnection.removeAllMessageListeners()
                        remoteConnection.addMessageListener(Client::messageListener)
                        //TODO Экран загрузки
                        val gameController = GameController(remoteConnection)
                        gameController.connection.blockUntilHaveMessages()
                        Thread.sleep(140L)
                        val oldController = Client.controller
                        Client.controller = gameController
                        Client.screen = gameController.screen
                        gameController.connection.checkNewMessages()
                        gameController.screen.updateResources()
                        Gdx.input.inputProcessor = gameController
                        oldController.dispose()
                    }
                }
            }
        }
        remoteConnection.run {
            addMessageListener(loginAnswerListener)
            sendAndFlush(LoginClientMessage(login, password))
            //TODO Добавить тайм-аут ожидания сообщения от сервера
            blockUntilHaveMessages()
            readOneMessage()//Вызывает loginAnswerListener
        }
        Client.checkMessagesOnRender = true
    }

    override fun dispose() {
        screen.dispose()
    }

    override val screen: Screen = MainMenuScreen()

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        startOnlineGame()
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        startOnlineGame()
        return true
    }
}*/