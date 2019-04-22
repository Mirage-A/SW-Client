package com.mirage.view.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.mirage.utils.Log
import com.mirage.utils.gameobjects.GameObjects
import com.mirage.utils.messaging.MoveDirection
import com.mirage.utils.datastructures.Point
import com.mirage.utils.extensions.*
import com.mirage.utils.gameobjects.Building
import com.mirage.utils.gameobjects.Entity
import com.mirage.utils.gameobjects.GameObject
import com.mirage.view.animation.BodyAction
import com.mirage.view.animation.LegsAction
import com.mirage.view.gameobjects.Drawers
import com.mirage.view.gameobjects.HumanoidAnimation
import java.util.*


/**
 * Интервал времени, который должен пройти с последней смены направления движения,
 * чтобы изменение отобразилось
 * (эта задержка убирает моргание анимации при быстром нажатии разных кнопок)
 */
private const val MOVE_DIRECTION_UPDATE_INTERVAL = 50L

/**
 * Отрисовывает все объекты карты
 */
fun renderObjects(batch: SpriteBatch, objs: GameObjects, drawers: Drawers) {

    val sortedObjs = depthSort(objs)

    for ((id, obj) in sortedObjs) {
        val isOpaque = isOpaque(obj, objs)
        val drawer = drawers[id] ?: run {
            Log.e("ERROR (ObjectRenderer::renderObjects): ObjectDrawer for object $id: $obj not found")
            null
        }
        //TODO Направление движения может влиять не только на HumanoidAnimation
        if (drawer is HumanoidAnimation) {
            val updatedMoveDirection = MoveDirection.fromString(obj.moveDirection ?: "DOWN")
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
        drawer?.draw(batch, Math.round(pos.x).toFloat(), Math.round(pos.y).toFloat())
    }
}

/**
 * Проверяет, нужно ли делать объект obj прозрачным (например, если за ним находится entity)
 * Возвращает true, если obj должен быть непрозрачным
 * [Building] становится прозрачным, если внутри него или на расстоянии tp-range тайлов за ним находится [Entity]
 * [Entity] не может быть прозрачным.
 */
private fun isOpaque(obj : GameObject, objs : GameObjects) : Boolean {
    if (obj is Building) {
        for ((_, other) in objs) {
            val rect = obj.rectangle
            val otherRect = other.rectangle
            if (other is Entity &&
                    -other.x + other.y + obj.x - obj.y < obj.transparencyRange * 2 &&
                    (rect.overlaps(otherRect) || compareEntityAndBuilding(other, obj) == -1))
                return false
        }
    }
    return true
}

/**
 * Сортировка списка объектов на карте по глубине (по порядку отрисовки)
 * Используется алгоритм топологической сортировки ориентированного графа
 * (На множестве объектов задан частичный порядок, а не линейный)
 */
private fun depthSort(objs: GameObjects) : List<Map.Entry<Long, GameObject>> {

    val objsList = objs.toMutableList()

    val q = ArrayDeque<Map.Entry<Long, GameObject>>()
    val visited = BooleanArray(objsList.size) {false}
    fun dfs(index: Int) {
        if (visited[index]) return
        visited[index] = true
        for (i in 0 until objsList.size) {
            if (compare(objsList[index].value, objsList[i].value) == 1) {
                dfs(i)
            }
        }
        q.add(objsList[index])
    }
    for (i in 0 until objsList.size) {
        dfs(i)
    }
    for (i in 0 until objsList.size) {
        objsList[i] = q.pop()
    }
    return objsList
}

/**
 * Возвращает 1, если точка p отрисовывается после прямоугольника rect
 * Возвращает -1, если точка p отрисовывается до прямоугольника rect
 * Возвращает 0, если объекты могут отрисовываться в любом относительном порядке
 * (т.е. объекты не сравнимы либо равны)
 */
private fun compare(p : Point, rect: Rectangle) : Int {
    /**
     * Находит значение функции f(x,y) = x + y - x0 - y0 для данной точки
     * Знак функции позволяет узнать расположение точки (x,y) относительно диагональной прямой,
     * проходящей через точку p
     */
    fun f(x: Float, y: Float) : Float = x + y - p.x - p.y

    //Если прямая и прямоугольник пересекаются
    if (f(rect.x, rect.y) <= 0 && f(rect.x + rect.width, rect.y + rect.height) >= 0) {
        //Вычисляем другую функцию - функцию от точки p относительно прямой, соединяющей 2 угловые точки прямоугольника
        val fun2 = - p.y * rect.width + p.x * rect.height + rect.y * rect.width - rect.x * rect.height
        if (fun2 > 0) return 1
        if (fun2 < 0) return -1
    }
    return 0
}

/**
 * Возвращает 1, если объект a отрисовывается после объекта b
 * Возвращает -1, если объект a отрисовывается до объекта b
 * Возвращает 0, если объекты могут отрисовываться в любом относительном порядке
 * (т.е. объекты не сравнимы либо равны)
 */
private fun compareDisjoint(a: GameObject, b: GameObject) : Int {
    val rectA = a.rectangle
    val rectB = b.rectangle
    val aIsPoint = rectA.width == 0f && rectA.height == 0f
    val bIsPoint = rectB.width == 0f && rectB.height == 0f
    if (!aIsPoint && !bIsPoint && rectA.overlaps(rectB)) return 0
    when {
        aIsPoint && bIsPoint -> {
            return -java.lang.Float.compare(getVirtualScreenPointFromScene(a.position).y,
                    getVirtualScreenPointFromScene(b.position).y)
        }
        aIsPoint && !bIsPoint -> {
            if (rectA.overlaps(rectB)) return -1
            return compare(Point(rectA.x, rectA.y), rectB)
        }
        !aIsPoint && bIsPoint -> {
            if (rectA.overlaps(rectB)) return 1
            return -compare(Point(rectB.x, rectB.y), rectA)
        }
        else -> {
            var res = compare(Point(rectA.x, rectA.y), rectB)
            if (res != 0) return res
            res = compare(Point(rectA.x + rectA.width, rectA.y + rectA.height), rectB)
            if (res != 0) return res
            res = compare(Point(rectB.x, rectB.y), rectA)
            if (res != 0) return -res
            res = compare(Point(rectB.x + rectB.width, rectB.y + rectB.height), rectA)
            if (res != 0) return -res
            return 0
        }
    }
}

private fun compareEntityAndBuilding(entity: Entity, building: Building) : Int {
    val rectA = entity.rectangle
    val rectB = building.rectangle
    val aIsPoint = rectA.width == 0f && rectA.height == 0f
    val bIsPoint = rectB.width == 0f && rectB.height == 0f
    if (rectA.overlaps(rectB)) return -1

    when {
        aIsPoint && bIsPoint -> {
            return -java.lang.Float.compare(getVirtualScreenPointFromScene(entity.position).y,
                    getVirtualScreenPointFromScene(building.position).y)
        }
        aIsPoint && !bIsPoint -> {
            if (rectA.overlaps(rectB)) return -1
            return compare(Point(rectA.x, rectA.y), rectB)
        }
        !aIsPoint && bIsPoint -> {
            if (rectA.overlaps(rectB)) return 1
            return -compare(Point(rectB.x, rectB.y), rectA)
        }
        else -> {
            var res = compare(Point(rectA.x, rectA.y), rectB)
            if (res != 0) return res
            res = compare(Point(rectA.x + rectA.width, rectA.y + rectA.height), rectB)
            if (res != 0) return res
            res = compare(Point(rectB.x, rectB.y), rectA)
            if (res != 0) return -res
            res = compare(Point(rectB.x + rectB.width, rectB.y + rectB.height), rectA)
            if (res != 0) return -res
            return 0
        }
    }
}

private fun compare(a: GameObject, b: GameObject) : Int =
        if (a is Entity && b is Building) compareEntityAndBuilding(a, b)
        else if (a is Building && b is Entity) -compareEntityAndBuilding(b, a)
        else compareDisjoint(a, b)
