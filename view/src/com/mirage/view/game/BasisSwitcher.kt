package com.mirage.view.game

import com.mirage.gamelogic.datastructures.Point
import com.mirage.view.screens.GameScreen


/**
 * Переход от базиса сцены (тайлы) к базису виртуального экрана (пиксели)
 */
fun getVirtualScreenPointFromScene(scenePoint: Point): Point {
    val x = GameScreen.TILE_WIDTH / 2 * scenePoint.x + GameScreen.TILE_WIDTH / 2 * scenePoint.y
    val y = -GameScreen.TILE_HEIGHT / 2 * scenePoint.x + GameScreen.TILE_HEIGHT / 2 * scenePoint.y + GameScreen.TILE_HEIGHT / 2
    return Point(x, y)
}

/**
 * Переход от базиса виртуального экрана (пиксели) к базису сцены (тайлы)
 */
fun getScenePointFromVirtualScreen(virtualScreenPoint: Point): Point {
    val x = virtualScreenPoint.x / GameScreen.TILE_WIDTH - (virtualScreenPoint.y - GameScreen.TILE_HEIGHT / 2) / GameScreen.TILE_HEIGHT
    val y = virtualScreenPoint.x / GameScreen.TILE_WIDTH + (virtualScreenPoint.y - GameScreen.TILE_HEIGHT / 2) / GameScreen.TILE_HEIGHT
    return Point(x, y)
}