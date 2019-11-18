package com.mirage.gameview

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.mirage.utils.Assets
import com.mirage.utils.DELTA_CENTER_Y
import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.game.states.StateDifference
import com.mirage.gameview.drawers.DrawersManager
import com.mirage.gameview.drawers.DrawersManagerImpl
import com.mirage.gameview.utils.getVirtualScreenPointFromScene

class GameViewImpl(private val gameMap: GameMap) : GameView {

    private val drawersManager: DrawersManager = DrawersManagerImpl()

    override fun loadDrawers(initialState: GameObjects) {
        drawersManager.loadDrawers(initialState)
    }

    override fun updateDrawers(oldState: GameObjects, diff: StateDifference) {
        drawersManager.updateDrawers(diff, oldState)
    }

    private val tileTexturesList: List<TextureRegion> = Assets.loadTileTexturesList(gameMap.tileSetName)

    override fun renderGameState(batch: SpriteBatch, objs: GameObjects, playerPositionOnScene: Point, virtualScreenWidth: Int, virtualScreenHeight: Int) {
        val playerPosOnVirtualScreen = getVirtualScreenPointFromScene(playerPositionOnScene)
        val cameraCenterPosition = Point(playerPosOnVirtualScreen.x, playerPosOnVirtualScreen.y + DELTA_CENTER_Y)
        renderGameMap(batch, gameMap, tileTexturesList, cameraCenterPosition.x, cameraCenterPosition.y, virtualScreenWidth, virtualScreenHeight)
        renderObjects(batch, objs, drawersManager, cameraCenterPosition.x, cameraCenterPosition.y)
    }

}