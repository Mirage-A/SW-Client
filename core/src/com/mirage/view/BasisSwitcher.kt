package com.mirage.view

import com.mirage.model.datastructures.Point
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
        val x = SceneView.TILE_WIDTH / 2 * scenePoint.x + SceneView.TILE_WIDTH / 2 * scenePoint.y +
                SceneView.X_MARGIN
        val y = -SceneView.TILE_HEIGHT / 2 * scenePoint.x + SceneView.TILE_HEIGHT / 2 * scenePoint.y +
                SceneView.Y_MARGIN + scene.width * SceneView.TILE_HEIGHT / 2
        return Point(x, y)
    }

    /**
     * Переводит координаты точки в базисе виртуального экрана в базис сцены
     * @param virtialScreenPoint Точка в базисе виртуального экрана
     * @param scene Сцена
     * @return Точка в базисе сцены
     */
    fun getScenePoint(virtialScreenPoint: Point, scene: Scene): Point {
        val x = virtialScreenPoint.x / SceneView.TILE_WIDTH - virtialScreenPoint.y / SceneView.TILE_HEIGHT -
                SceneView.X_MARGIN / SceneView.TILE_WIDTH + SceneView.Y_MARGIN / SceneView.TILE_HEIGHT + (scene.width / 2).toFloat()
        val y = virtialScreenPoint.x / SceneView.TILE_WIDTH + virtialScreenPoint.y / SceneView.TILE_HEIGHT -
                SceneView.X_MARGIN / SceneView.TILE_WIDTH - SceneView.Y_MARGIN / SceneView.TILE_HEIGHT - (scene.width / 2).toFloat()
        return Point(x, y)
    }
}
