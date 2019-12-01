package com.mirage.gameview

import com.mirage.gameview.drawers.DrawersManager
import com.mirage.gameview.drawers.DrawersManagerImpl
import com.mirage.gameview.utils.getVirtualScreenPointFromScene
import com.mirage.utils.DELTA_CENTER_Y
import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.oldobjects.GameObjects
import com.mirage.utils.virtualscreen.VirtualScreen

class GameViewImpl(private val gameMap: GameMap) : GameView {

    private val drawersManager: DrawersManager = DrawersManagerImpl()

    override fun loadDrawers(initialState: GameObjects) {
        drawersManager.loadDrawers(initialState)
    }

    override fun updateDrawers(oldState: GameObjects, diff: StateDifference) {
        drawersManager.updateDrawers(diff, oldState)
    }

    override fun renderGameState(virtualScreen: VirtualScreen, objs: GameObjects, playerPositionOnScene: Point) {
        val playerPosOnVirtualScreen = getVirtualScreenPointFromScene(playerPositionOnScene)
        val cameraCenterPosition = Point(playerPosOnVirtualScreen.x, playerPosOnVirtualScreen.y + DELTA_CENTER_Y)
        renderGameMap(virtualScreen, gameMap, cameraCenterPosition.x, cameraCenterPosition.y)
        renderObjects(virtualScreen, objs, drawersManager, cameraCenterPosition.x, cameraCenterPosition.y)
    }

}