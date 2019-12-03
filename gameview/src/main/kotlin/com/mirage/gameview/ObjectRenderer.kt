package com.mirage.gameview

import com.mirage.gameview.drawers.DrawersManager
import com.mirage.gameview.utils.compareEntityAndBuilding
import com.mirage.gameview.utils.depthSort
import com.mirage.gameview.utils.getVirtualScreenPointFromScene
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.game.objects.simplified.SimplifiedBuilding
import com.mirage.utils.game.objects.simplified.SimplifiedEntity
import com.mirage.utils.game.objects.simplified.SimplifiedObject
import com.mirage.utils.game.states.SimplifiedState
import com.mirage.utils.virtualscreen.VirtualScreen
import kotlin.math.abs


/**
 * Отрисовывает все объекты карты
 */
internal fun renderGameState(virtualScreen: VirtualScreen, state: SimplifiedState, drawersManager: DrawersManager, cameraX: Float, cameraY: Float, targetID: Long?) {

    val sortedObjs: MutableList<Pair<Long, SimplifiedObject>> = (state.buildings.toList() + state.entities.toList()).toMutableList()
    depthSort(sortedObjs)
    val currentTimeMillis = System.currentTimeMillis()
    for ((id, obj) in sortedObjs) {
        val isOpaque = isOpaque(obj, state)
        val pos = getVirtualScreenPointFromScene(obj.position)
        when (obj) {
            is SimplifiedBuilding ->
                drawersManager.drawBuilding(id, virtualScreen, (pos.x - cameraX), (pos.y - cameraY), isOpaque, currentTimeMillis)
            is SimplifiedEntity -> {
                if (id == targetID) {
                    val hitBox = drawersManager.getEntityHitbox(id) ?: Rectangle()
                    virtualScreen.draw("ui/target-circle-back", pos.x - cameraX, pos.y - cameraY,
                            hitBox.width, 64f)
                    drawersManager.drawEntity(id, virtualScreen, (pos.x - cameraX), (pos.y - cameraY), isOpaque, currentTimeMillis, obj.moveDirection)

                    virtualScreen.draw("ui/target-circle-front", pos.x - cameraX, pos.y - cameraY,
                            hitBox.width, 64f)
                    val arrowDelta = abs(20f - (currentTimeMillis / 10L % 100L).toFloat() / 2.5f)
                    virtualScreen.draw("ui/target-arrow",
                            pos.x - cameraX, pos.y - cameraY + hitBox.height + 32f + 12f + arrowDelta)
                }
                else {
                    drawersManager.drawEntity(id, virtualScreen, (pos.x - cameraX), (pos.y - cameraY), isOpaque, currentTimeMillis, obj.moveDirection)
                }
            }
        }

    }
}

/**
 * Проверяет, нужно ли делать объект obj прозрачным (например, если за ним находится entity)
 * Возвращает true, если obj должен быть непрозрачным
 * Строение становится прозрачным, если внутри него или на расстоянии tp-range тайлов за ним находится сущность.
 * Сущность не может быть прозрачной.
 */
private fun isOpaque(obj: SimplifiedObject, state: SimplifiedState) : Boolean {
    if (obj is SimplifiedBuilding) {
        for ((_, entity) in state.entities) {
            val rect = obj.rectangle
            val entityRect = entity.rectangle
            //TODO Придумать нормальный алгоритм проверки, нужно ли скрывать строение
            if ((-entity.x + entity.y + obj.x - obj.y < obj.transparencyRange * 2 &&
                    compareEntityAndBuilding(entity, obj) == -1) || rect.overlaps(entityRect))
                return false
        }
    }
    return true
}
