package com.mirage.gameview

import com.mirage.gameview.drawers.DrawersManager
import com.mirage.gameview.utils.compareEntityAndBuilding
import com.mirage.gameview.utils.depthSort
import com.mirage.gameview.utils.getVirtualScreenPointFromScene
import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.virtualscreen.VirtualScreen
import kotlin.math.roundToInt


/**
 * Отрисовывает все объекты карты
 */
internal fun renderObjects(virtualScreen: VirtualScreen, objs: GameObjects, drawersManager: DrawersManager, cameraX: Float, cameraY: Float) {

    val sortedObjs = depthSort(objs)
    val currentTimeMillis = System.currentTimeMillis()

    for ((id, obj) in sortedObjs) {
        val isOpaque = isOpaque(obj, objs)
        val pos = getVirtualScreenPointFromScene(obj.position)
        drawersManager.draw(id, virtualScreen,
                (pos.x - cameraX),
                (pos.y - cameraY),
                isOpaque, currentTimeMillis, obj.moveDirection)
    }
}

/**
 * Проверяет, нужно ли делать объект obj прозрачным (например, если за ним находится entity)
 * Возвращает true, если obj должен быть непрозрачным
 * Строение становится прозрачным, если внутри него или на расстоянии tp-range тайлов за ним находится сущность.
 * Сущность не может быть прозрачной.
 */
private fun isOpaque(obj : GameObject, objs : GameObjects) : Boolean {
    if (obj.type == GameObject.Type.BUILDING) {
        for ((_, other) in objs) {
            val rect = obj.rectangle
            val otherRect = other.rectangle
            if (other.type == GameObject.Type.ENTITY &&
                    ((-other.x + other.y + obj.x - obj.y < obj.transparencyRange * 2 &&
                    compareEntityAndBuilding(other, obj) == -1) || rect.overlaps(otherRect)))
                return false
        }
    }
    return true
}
