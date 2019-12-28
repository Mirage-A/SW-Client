package com.mirage.view

import com.mirage.core.utils.Point
import com.mirage.core.utils.Rectangle
import com.mirage.core.utils.rangeBetween
import com.mirage.core.game.objects.SimplifiedBuilding
import com.mirage.core.game.objects.SimplifiedEntity
import com.mirage.core.game.objects.SimplifiedObject
import com.mirage.core.game.states.SimplifiedState
import com.mirage.core.virtualscreen.VirtualScreen
import com.mirage.view.drawers.DrawersManager
import com.mirage.view.utils.compareEntityAndBuilding
import com.mirage.view.utils.depthSort
import com.mirage.view.utils.getVirtualScreenPointFromScene
import kotlin.math.abs


/**
 * Отрисовывает все объекты карты
 */
internal fun renderGameState(virtualScreen: VirtualScreen, state: SimplifiedState, drawersManager: DrawersManager, cameraX: Float, cameraY: Float, playerPositionOnScene: Point, targetID: Long?, isTargetEnemy: Boolean) {

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
                    val targetRelation = if (isTargetEnemy) "enemy" else "ally"
                    val hitBox = drawersManager.getEntityHitbox(id) ?: Rectangle()
                    virtualScreen.draw("ui/scene/target-$targetRelation-circle-back", pos.x - cameraX, pos.y - cameraY,
                            hitBox.width, 64f)
                    drawersManager.drawEntity(id, virtualScreen, (pos.x - cameraX), (pos.y - cameraY), isOpaque, currentTimeMillis, obj.moveDirection)

                    virtualScreen.draw("ui/scene/target-$targetRelation-circle-front", pos.x - cameraX, pos.y - cameraY,
                            hitBox.width, 64f)
                    val arrowDelta = abs(20f - (currentTimeMillis / 10L % 100L).toFloat() / 2.5f)
                    virtualScreen.draw("ui/scene/target-$targetRelation-arrow",
                            pos.x - cameraX, pos.y - cameraY + hitBox.height + 32f + 32f + arrowDelta)
                } else {
                    drawersManager.drawEntity(id, virtualScreen, (pos.x - cameraX), (pos.y - cameraY), isOpaque, currentTimeMillis, obj.moveDirection)
                }
                if (obj.interactionRange > 0f) {
                    val hitBox = drawersManager.getEntityHitbox(id) ?: Rectangle()
                    val interactionBoxY = pos.y - cameraY + hitBox.height + 9f + 8f
                    val range = rangeBetween(playerPositionOnScene, obj.position)
                    val modifier = if (id == targetID) {
                        if (range < obj.interactionRange) "opaque" else "transparent"
                    } else {
                        if (range < obj.interactionRange) "transparent" else "super-transparent"
                    }
                    val textureName = "ui/scene/interact-$modifier"
                    virtualScreen.draw(textureName, pos.x - cameraX, interactionBoxY)
                    /*if (id == targetID) {
                        virtualScreen.draw("ui/scene/interact-btn-$modifier", pos.x - cameraX - 46f - 18f, interactionBoxY)
                    }*/
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
private fun isOpaque(obj: SimplifiedObject, state: SimplifiedState): Boolean {
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
