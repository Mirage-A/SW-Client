package com.mirage.view

import com.mirage.model.scene.Point
import com.mirage.model.scene.Scene

object BasisSwitcher {

    /**
     * Переводит координаты точки в базисе сцены в базис камеры
     * @param scenePoint Точка в базисе сцены
     * @param scene Сцена
     * @param scrX Координаты экрана
     * @param scrY Координаты экрана
     * @return Точка в базисе камеры
     */
    fun getViewportPointFromScene(scenePoint: Point, scene: Scene, scrX: Float, scrY: Float): Point {
        return getViewportPointFromVirtualScreen(getVirtualScreenPoint(scenePoint, scene), scrX, scrY)
    }

    /**
     * Переводит координаты точки в базисе виртуального экрана в базис камеры
     * @param virtualScreenPoint Точка в базисе виртуального экрана
     * @param scrX Координаты экрана
     * @param scrY Координаты экрана
     * @return Точка в базисе камеры
     */
    fun getViewportPointFromVirtualScreen(virtualScreenPoint: Point, scrX: Float, scrY: Float): Point {
        return Point(virtualScreenPoint.x - scrX, virtualScreenPoint.y - scrY)
    }

    /**
     * Переводит координаты точки в базисе сцены в базис виртуального экрана
     * @param scenePoint Точка в базисе сцены
     * @param scene Сцена
     * @return Точка в базисе виртуального экрана
     */
    fun getVirtualScreenPoint(scenePoint: Point, scene: Scene): Point {
        val x = View.TILE_WIDTH / 2 * scenePoint.x + View.TILE_WIDTH / 2 * scenePoint.y +
                View.X_MARGIN
        val y = -View.TILE_HEIGHT / 2 * scenePoint.x + View.TILE_HEIGHT / 2 * scenePoint.y +
                View.Y_MARGIN + scene.width * View.TILE_HEIGHT / 2
        return Point(x, y)
    }

    /**
     * Переводит координаты точки в базисе виртуального экрана в базис сцены
     * @param virtialScreenPoint Точка в базисе виртуального экрана
     * @param scene Сцена
     * @return Точка в базисе сцены
     */
    fun getScenePoint(virtialScreenPoint: Point, scene: Scene): Point {
        val x = virtialScreenPoint.x / View.TILE_WIDTH - virtialScreenPoint.y / View.TILE_HEIGHT -
                View.X_MARGIN / View.TILE_WIDTH + View.Y_MARGIN / View.TILE_HEIGHT + (scene.width / 2).toFloat()
        val y = virtialScreenPoint.x / View.TILE_WIDTH + virtialScreenPoint.y / View.TILE_HEIGHT -
                View.X_MARGIN / View.TILE_WIDTH - View.Y_MARGIN / View.TILE_HEIGHT - (scene.width / 2).toFloat()
        return Point(x, y)
    }
}
