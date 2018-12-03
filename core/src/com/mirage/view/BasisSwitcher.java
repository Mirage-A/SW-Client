package com.mirage.view;

import com.mirage.model.scene.Point;
import com.mirage.model.scene.Scene;

public class BasisSwitcher {

    /**
     * Переводит координаты точки в базисе сцены в базис камеры
     * @param scenePoint Точка в базисе сцены
     * @param scene Сцена
     * @param scrX Координаты экрана
     * @param scrY Координаты экрана
     * @return Точка в базисе камеры
     */
    public static Point getViewportPointFromScene(Point scenePoint, Scene scene, float scrX, float scrY) {
        return getViewportPointFromVirtualScreen(getVirtualScreenPoint(scenePoint, scene), scrX, scrY);
    }

    /**
     * Переводит координаты точки в базисе виртуального экрана в базис камеры
     * @param virtualScreenPoint Точка в базисе виртуального экрана
     * @param scrX Координаты экрана
     * @param scrY Координаты экрана
     * @return Точка в базисе камеры
     */
    public static Point getViewportPointFromVirtualScreen(Point virtualScreenPoint, float scrX, float scrY) {
        return new Point(virtualScreenPoint.getX() - scrX, virtualScreenPoint.getY() - scrY);
    }

    /**
     * Переводит координаты точки в базисе сцены в базис виртуального экрана
     * @param scenePoint Точка в базисе сцены
     * @param scene Сцена
     * @return Точка в базисе виртуального экрана
     */
    public static Point getVirtualScreenPoint(Point scenePoint, Scene scene) {
        float x = View.TILE_WIDTH / 2 * scenePoint.getX() + View.TILE_WIDTH / 2 * scenePoint.getY() +
                View.X_MARGIN;
        float y = - View.TILE_HEIGHT / 2 * scenePoint.getX() + View.TILE_HEIGHT / 2 * scenePoint.getY() +
                View.Y_MARGIN + scene.getWidth() * View.TILE_HEIGHT / 2;
        return new Point(x, y);
    }

    /**
     * Переводит координаты точки в базисе виртуального экрана в базис сцены
     * @param virtialScreenPoint Точка в базисе виртуального экрана
     * @param scene Сцена
     * @return Точка в базисе сцены
     */
    public static Point getScenePoint(Point virtialScreenPoint, Scene scene) {
        float x = virtialScreenPoint.getX() / View.TILE_WIDTH - virtialScreenPoint.getY() / View.TILE_HEIGHT -
                View.X_MARGIN / View.TILE_WIDTH + View.Y_MARGIN / View.TILE_HEIGHT + scene.getWidth() / 2;
        float y = virtialScreenPoint.getX() / View.TILE_WIDTH + virtialScreenPoint.getY() / View.TILE_HEIGHT -
                View.X_MARGIN / View.TILE_WIDTH - View.Y_MARGIN / View.TILE_HEIGHT - scene.getWidth() / 2;
        return new Point(x, y);
    }
}
