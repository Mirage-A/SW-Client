package com.mirage.gameview

import com.mirage.gameview.utils.getScenePointFromVirtualScreen
import com.mirage.gameview.utils.getVirtualScreenPointFromScene
import com.mirage.utils.TILE_WIDTH
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.trunc
import com.mirage.utils.game.maps.GameMap
import com.mirage.utils.virtualscreen.VirtualScreen

/** Renders game map tiles */
fun renderGameMap(virtualScreen: VirtualScreen, gameMap: GameMap, cameraX: Float, cameraY: Float) {
    //Находим крайние координаты тайлов, которые помещаются на экран. Другие тайлы рисовать смысла нет.
    val w = virtualScreen.width / 2f
    val h = virtualScreen.height / 2f
    val x1 = getScenePointFromVirtualScreen(Point(cameraX - w, cameraY + h)).x.trunc()
    val y1 = getScenePointFromVirtualScreen(Point(cameraX - w, cameraY - h)).y.trunc()
    val x2 = getScenePointFromVirtualScreen(Point(cameraX + w, cameraY - h)).x.trunc()
    val y2 = getScenePointFromVirtualScreen(Point(cameraX + w, cameraY + h)).y.trunc()
    for (i in x1..x2) {
        for (j in y1..y2) {
            val virtualCenterPoint = getVirtualScreenPointFromScene(Point(i.toFloat(), j.toFloat()))
            val textureX = virtualCenterPoint.x - cameraX + TILE_WIDTH / 2
            val textureY = virtualCenterPoint.y - cameraY
            try {
                if (i in 0 until gameMap.width && j in 0 until gameMap.height) {
                    virtualScreen.drawTile(gameMap.getTileID(i, j), textureX, textureY)
                }
                else {
                    virtualScreen.drawTile(gameMap.defaultTileID, textureX, textureY)
                }
            }
            catch(ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}