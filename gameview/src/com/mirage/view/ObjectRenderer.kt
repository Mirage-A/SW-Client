package com.mirage.view

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mirage.utils.Log
import com.mirage.utils.game.objects.GameObjects
import com.mirage.utils.game.objects.GameObject
import com.mirage.view.objectdrawers.animation.BodyAction
import com.mirage.view.objectdrawers.animation.LegsAction
import com.mirage.view.objectdrawers.Drawers
import com.mirage.view.objectdrawers.HumanoidAnimation
import com.mirage.view.utils.compareEntityAndBuilding
import com.mirage.view.utils.depthSort
import com.mirage.view.utils.getVirtualScreenPointFromScene
import kotlin.math.roundToInt


/**
 * Интервал времени, который должен пройти с последней смены направления движения,
 * чтобы изменение отобразилось
 * (эта задержка убирает моргание анимации при быстром нажатии разных кнопок)
 */
private const val MOVE_DIRECTION_UPDATE_INTERVAL = 50L

/**
 * Отрисовывает все объекты карты
 */
internal fun renderObjects(batch: SpriteBatch, objs: GameObjects, drawers: Drawers, cameraX: Float, cameraY: Float) {

    val sortedObjs = depthSort(objs)

    for ((id, obj) in sortedObjs) {
        val isOpaque = isOpaque(obj, objs)
        val drawer = drawers[id] ?: run {
            Log.e("ERROR (ObjectRenderer::renderObjects): ObjectDrawer for object $id: $obj not found")
            null
        }
        //TODO Направление движения может влиять не только на HumanoidAnimation
        if (drawer is HumanoidAnimation) {
            val updatedMoveDirection = obj.moveDirection
            if (updatedMoveDirection !== drawer.bufferedMoveDirection) {
                drawer.lastMoveDirectionUpdateTime = System.currentTimeMillis()
                drawer.bufferedMoveDirection = updatedMoveDirection
            } else if (System.currentTimeMillis() - drawer.lastMoveDirectionUpdateTime > MOVE_DIRECTION_UPDATE_INTERVAL) {
                drawer.moveDirection = drawer.bufferedMoveDirection
            }

            val isMoving = obj.isMoving
            if (isMoving) {
                drawer.setBodyAction(BodyAction.RUNNING)
                drawer.setLegsAction(LegsAction.RUNNING)
            } else {
                drawer.setBodyAction(BodyAction.IDLE)
                drawer.setLegsAction(LegsAction.IDLE)
            }
        }
        val pos = getVirtualScreenPointFromScene(obj.position)
        drawer?.draw(batch, (pos.x - cameraX).roundToInt().toFloat(), (pos.y - cameraY).roundToInt().toFloat())
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
                    -other.x + other.y + obj.x - obj.y < obj.transparencyRange * 2 &&
                    (rect.overlaps(otherRect) || compareEntityAndBuilding(other, obj) == -1))
                return false
        }
    }
    return true
}
