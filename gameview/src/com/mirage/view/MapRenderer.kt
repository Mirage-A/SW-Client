package com.mirage.view

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.utils.TILE_HEIGHT
import com.mirage.utils.TILE_WIDTH
import com.mirage.utils.datastructures.Point
import com.mirage.utils.game.maps.GameMap
import com.mirage.view.objectdrawers.Image
import com.mirage.view.utils.getVirtualScreenPointFromScene
import java.lang.Exception

/**
 * Отрисовывает тайлы карты
 * Для каждого объекта [Image] в списке [tileSet] предполагается, что alignment == BOTTOM_LEFT
 */
fun renderGameMap(batch: SpriteBatch, gameMap: GameMap, tileSet: List<Image>) {
    for (i in 0 until gameMap.width) {
        for (j in 0 until gameMap.height) {
            val virtualCenterPoint = getVirtualScreenPointFromScene(Point(i + 0.5f, j + 0.5f))
            val textureX = virtualCenterPoint.x - TILE_WIDTH / 2
            val textureY = virtualCenterPoint.y - TILE_HEIGHT / 2
            try {
                tileSet[gameMap.getTileID(i, j)].draw(batch, textureX, textureY)
            }
            catch(ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}