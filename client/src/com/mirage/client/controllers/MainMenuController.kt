package com.mirage.client.controllers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.mirage.client.Client
import com.mirage.utils.ONLINE_MODE
import com.mirage.view.screens.LoadingScreen
import com.mirage.view.screens.MainMenuScreen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainMenuController : Controller {

    /**
     * Начать игру
     *   //TODO выбор режима - онлайн/оффлайн
     *   //TODO Исправить костыль с ожиданием загрузки всех объектов - возможно, добавить новый тип сообщения, которое логика отправляет
     *   //TODO новому игроку после отправки информации об объектах на карте.
     *   //TODO Ну или загружать объекты карты из файла, а не ждать сообщений от логики.
     */
    fun startGame(isOnline: Boolean) {
        Client.checkMessagesOnRender = false
        val gameController = GameController()
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
        startGame(ONLINE_MODE)
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        startGame(ONLINE_MODE)
        return true
    }
}