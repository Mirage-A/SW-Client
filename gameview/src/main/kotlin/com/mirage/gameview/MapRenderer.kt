package com.mirage.gameview

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.mirage.utils.TILE_HEIGHT
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.trunc
import com.mirage.utils.game.maps.GameMap
import com.mirage.gameview.utils.getScenePointFromVirtualScreen
import com.mirage.gameview.utils.getVirtualScreenPointFromScene

/**
 * Отрисовывает тайлы карты
 * Для каждого объекта [Image] в списке [tileTexturesList] предполагается, что alignment == BOTTOM_LEFT
 */
fun renderGameMap(batch: SpriteBatch, gameMap: GameMap, tileTexturesList: List<TextureRegion>, cameraX: Float, cameraY: Float, virtualScreenWidth: Int, virtualScreenHeight: Int) {
    //Находим крайние координаты тайлов, которые помещаются на экран. Другие тайлы рисовать смысла нет.
    val w = virtualScreenWidth / 2f
    val h = virtualScreenHeight / 2f
    val x1 = getScenePointFromVirtualScreen(Point(cameraX - w, cameraY + h)).x.trunc()
    val y1 = getScenePointFromVirtualScreen(Point(cameraX - w, cameraY - h)).y.trunc()
    val x2 = getScenePointFromVirtualScreen(Point(cameraX + w, cameraY - h)).x.trunc()
    val y2 = getScenePointFromVirtualScreen(Point(cameraX + w, cameraY + h)).y.trunc()
    for (i in x1..x2) {
        for (j in y1..y2) {
            val virtualCenterPoint = getVirtualScreenPointFromScene(Point(i.toFloat(), j.toFloat()))
            val textureX = virtualCenterPoint.x - cameraX
            val textureY = virtualCenterPoint.y - cameraY - TILE_HEIGHT / 2
            try {
                if (i in 0 until gameMap.width && j in 0 until gameMap.height) {
                    batch.draw(tileTexturesList[gameMap.getTileID(i, j)], textureX, textureY)
                }
                else {
                    batch.draw(tileTexturesList[gameMap.defaultTileID], textureX, textureY)
                }
            }
            catch(ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}