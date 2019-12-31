package com.mirage.view.utils

import com.mirage.core.TILE_HEIGHT
import com.mirage.core.TILE_WIDTH
import com.mirage.core.utils.Point


fun getVirtualScreenPointFromScene(scenePoint: Point): Point {
    val x = TILE_WIDTH / 2 * scenePoint.x + TILE_WIDTH / 2 * scenePoint.y
    val y = -TILE_HEIGHT / 2 * scenePoint.x + TILE_HEIGHT / 2 * scenePoint.y
    return Point(x, y)
}

fun getScenePointFromVirtualScreen(virtualScreenPoint: Point): Point {
    val x = virtualScreenPoint.x / TILE_WIDTH - virtualScreenPoint.y / TILE_HEIGHT
    val y = virtualScreenPoint.x / TILE_WIDTH + virtualScreenPoint.y / TILE_HEIGHT
    return Point(x, y)
}