package com.mirage.view.game

import com.mirage.utils.datastructures.MutablePoint
import com.mirage.view.screens.GameScreen


/**
 * Переход от базиса сцены (тайлы) к базису виртуального экрана (пиксели)
 */
fun getVirtualScreenPointFromScene(scenePoint: MutablePoint): MutablePoint {
    val x = GameScreen.TILE_WIDTH / 2 * scenePoint.x + GameScreen.TILE_WIDTH / 2 * scenePoint.y
    val y = -GameScreen.TILE_HEIGHT / 2 * scenePoint.x + GameScreen.TILE_HEIGHT / 2 * scenePoint.y + GameScreen.TILE_HEIGHT / 2
    return MutablePoint(x, y)
}

/**
 * Переход от базиса виртуального экрана (пиксели) к базису сцены (тайлы)
 */
fun getScenePointFromVirtualScreen(virtualScreenPoint: MutablePoint): MutablePoint {
    val x = virtualScreenPoint.x / GameScreen.TILE_WIDTH - (virtualScreenPoint.y - GameScreen.TILE_HEIGHT / 2) / GameScreen.TILE_HEIGHT
    val y = virtualScreenPoint.x / GameScreen.TILE_WIDTH + (virtualScreenPoint.y - GameScreen.TILE_HEIGHT / 2) / GameScreen.TILE_HEIGHT
    return MutablePoint(x, y)
}