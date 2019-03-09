package com.mirage.view.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Rectangle
import com.mirage.gamelogic.datastructures.Point
import com.mirage.gamelogic.extensions.*
import com.mirage.view.animation.BodyAction
import com.mirage.view.animation.LegsAction
import com.mirage.gamelogic.MoveDirection
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
fun renderObjects(batch: SpriteBatch, map: TiledMap, drawers: Drawers) {
    for (layer in map.layers) {
        val objs = ArrayList<MapObject>()

        for (obj in layer.objects) {
            objs.add(obj)
        }

        depthSort(objs)

        for (obj in objs) {
            val isOpaque = isOpaque(obj, map)
            val drawer = drawers[obj, isOpaque] ?:
                drawers.apply { addObjectDrawer(obj) }[obj, isOpaque]
            //TODO Направление движения может влиять не только на HumanoidAnimation
            if (drawer is HumanoidAnimation) {
                val updatedMoveDirection = MoveDirection.fromMoveAngle(obj.moveAngle)
                if (updatedMoveDirection !== drawer.bufferedMoveDirection) {
                    drawer.lastMoveDirectionUpdateTime = System.currentTimeMillis()
                    drawer.bufferedMoveDirection = updatedMoveDirection
                } else if (System.currentTimeMillis() - drawer.lastMoveDirectionUpdateTime > MOVE_DIRECTION_UPDATE_INTERVAL) {
                    drawer.moveDirection = drawer.bufferedMoveDirection
                }

                if (obj.isMoving) {
                    drawer.setBodyAction(BodyAction.RUNNING)
                    drawer.setLegsAction(LegsAction.RUNNING)
                } else {
                    drawer.setBodyAction(BodyAction.IDLE)
                    drawer.setLegsAction(LegsAction.IDLE)
                }
            }
            //val pos = getVirtualScreenPointFromScene(obj.getPosition())
            val scenePoint = obj.position
            val width = obj.properties.getFloat("width", 0f)
            val height = obj.properties.getFloat("height", 0f)
            val sceneCenter = Point(scenePoint.x + width / 2, scenePoint.y + height / 2)
            val pos = getVirtualScreenPointFromScene(sceneCenter)
            drawer?.draw(batch, Math.round(pos.x).toFloat(), Math.round(pos.y).toFloat())
        }
    }
}

/**
 * Проверяет, нужно ли делать объект obj прозрачным (например, если за ним находится entity)
 * Возвращает true, если obj должен быть непрозрачным
 * Объект становится прозрачным, если у него есть свойство tp-range и
 * внутри этого объекта или на расстоянии tp-range тайлов за объектом находится entity
 */
private fun isOpaque(obj: MapObject, map: TiledMap) : Boolean {
    for (other in map) {
        val rect = obj.rectangle
        val otherRect = other.rectangle
        val center = rect.center
        val otherCenter = otherRect.center
        if (other.properties.getString("type", "") == "entity" &&
                - otherCenter.x + otherCenter.y + center.x - center.y < obj.properties.getFloat("tp-range") * 2 &&
                (rect.overlaps(otherRect) || compareEntityAndBuilding(other, obj) == -1))
            return false
    }
    return true
}

/**
 * Сортировка списка объектов на карте по глубине (по порядку отрисовки)
 * Используется алгоритм топологической сортировки ориентированного графа
 * (На множестве объектов задан частичный порядок, а не линейный)
 */
private fun depthSort(objs: ArrayList<MapObject>) {

    val q = ArrayDeque<MapObject>()
    val visited = BooleanArray(objs.size) {false}
    fun dfs(index: Int) {
        if (visited[index]) return
        visited[index] = true
        for (i in 0 until objs.size) {
            if (compare(objs[index], objs[i]) == 1) {
                dfs(i)
            }
        }
        q.add(objs[index])
    }
    for (i in 0 until objs.size) {
        dfs(i)
    }
    for (i in 0 until objs.size) {
        objs[i] = q.pop()
    }
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
private fun compareDisjoint(a: MapObject, b: MapObject) : Int {
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

private fun compareEntityAndBuilding(entity: MapObject, building: MapObject) : Int {
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

private fun compare(a: MapObject, b: MapObject) : Int {
    val typeA = a.properties["type"]
    val typeB = b.properties["type"]
    if (typeA == typeB) return compareDisjoint(a, b)
    if (typeA == "entity") return compareEntityAndBuilding(a, b)
    if (typeB == "entity") return -compareEntityAndBuilding(b, a)
    return compareDisjoint(a, b)
}
