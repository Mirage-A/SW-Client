package com.mirage.model

import com.mirage.model.scene.Point
import com.mirage.model.scene.Scene


class ModelFacade {
    private val gameLogic = LogicThread()

    fun startGame() {
        gameLogic.run()
    }

    fun getScene() : Scene = gameLogic.scene

    fun getPlayerPosition() : Point {
        return Point(gameLogic.scene.playerX, gameLogic.scene.playerY)
    }
}