package com.mirage.view

import com.mirage.model.datastructures.Point
import com.mirage.model.scene.Scene
import com.mirage.view.screens.GameScreen

/**
 * Содержит методы для перехода между тремя базисами:
 * Базис сцены, с которым работает модель
 * Базис виртуального экрана, начало которого совпадает с началом базиса сцены
 * Базис камеры, который используется для отрисовки и двигается по базису виртуального экрана
 */
object BasisSwitcher {

    /**
     * Переводит координаты точки в базисе сцены в базис камеры
     * @param scenePoint Точка в базисе сцены
     * @param scene Сцена
     * @param scrX Координаты камеры на виртуальном экране
     * @param scrY Координаты камеры на виртуальном экране
     * @return Точка в базисе камеры
     */
    fun getViewportPointFromScene(scenePoint: Point, scene: Scene, scrX: Float, scrY: Float): Point {
        return getViewportPointFromVirtualScreen(getVirtualScreenPointFromScene(scenePoint, scene), scrX, scrY)
    }

    /**
     * Переводит координаты точки в базисе виртуального экрана в базис камеры
     * @param virtualScreenPoint Точка в базисе виртуального экрана
     * @param scrX Координаты камеры на виртуальном экране
     * @param scrY Координаты камеры на виртуальном экране
     * @return Точка в базисе камеры
     */
    fun getViewportPointFromVirtualScreen(virtualScreenPoint: Point, scrX: Float, scrY: Float): Point {
        return Point(virtualScreenPoint.x - scrX, virtualScreenPoint.y - scrY)
    }


    /**
     * Переводит координаты точки в базисе камеры в базис виртуального экрана
     * @param viewportPoint Точка в базисе камеры
     * @param scrX Координаты камеры на виртуальном экране
     * @param scrY Координаты камеры на виртуальном экране
     * @return Точка в базисе виртуального экрана
     */
    fun getVirtualScreenPointFromViewport(viewportPoint: Point, scrX: Float, scrY: Float): Point {
        return Point(viewportPoint.x + scrX, viewportPoint.y + scrY)
    }

    /**
     * Переводит координаты точки в базисе сцены в базис виртуального экрана
     * @param scenePoint Точка в базисе сцены
     * @param scene Сцена
     * @return Точка в базисе виртуального экрана
     */
    fun getVirtualScreenPointFromScene(scenePoint: Point, scene: Scene): Point {
        val x = GameScreen.TILE_WIDTH / 2 * scenePoint.x + GameScreen.TILE_WIDTH / 2 * scenePoint.y +
                GameScreen.X_MARGIN
        val y = -GameScreen.TILE_HEIGHT / 2 * scenePoint.x + GameScreen.TILE_HEIGHT / 2 * scenePoint.y +
                GameScreen.Y_MARGIN + scene.width * GameScreen.TILE_HEIGHT / 2
        return Point(x, y)
    }

    /**
     * Переводит координаты точки в базисе виртуального экрана в базис сцены
     * @param virtualScreenPoint Точка в базисе виртуального экрана
     * @param scene Сцена
     * @return Точка в базисе сцены
     */
    fun getScenePointFromVirtualScreen(virtualScreenPoint: Point, scene: Scene): Point {
        val x = virtualScreenPoint.x / GameScreen.TILE_WIDTH - virtualScreenPoint.y / GameScreen.TILE_HEIGHT -
                GameScreen.X_MARGIN / GameScreen.TILE_WIDTH + GameScreen.Y_MARGIN / GameScreen.TILE_HEIGHT + (scene.width / 2).toFloat()
        val y = virtualScreenPoint.x / GameScreen.TILE_WIDTH + virtualScreenPoint.y / GameScreen.TILE_HEIGHT -
                GameScreen.X_MARGIN / GameScreen.TILE_WIDTH - GameScreen.Y_MARGIN / GameScreen.TILE_HEIGHT - (scene.width / 2).toFloat()
        return Point(x, y)
    }

    /**
     * Переводит координаты точки в базисе камеры в базис сцены
     * @param viewportPoint Точка в базисе камеры
     * @param scene Сцена
     * @param scrX Координаты камеры на виртуальном экране
     * @param scrY Координаты камеры на виртуальном экране
     * @return Точка в базисе сцены
     */
    fun getScenePointFromViewport(viewportPoint: Point, scene: Scene, scrX: Float, scrY: Float) : Point {
        return getScenePointFromVirtualScreen(getVirtualScreenPointFromViewport(viewportPoint, scrX, scrY), scene)
    }
}
