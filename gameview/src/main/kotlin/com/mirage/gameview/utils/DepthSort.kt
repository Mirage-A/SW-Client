package com.mirage.gameview.utils

import com.mirage.utils.Log
import com.mirage.utils.datastructures.Point
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.game.objects.GameObjects
import java.util.*


/**
 * Сортировка списка объектов на карте по глубине (по порядку отрисовки)
 * Используется алгоритм топологической сортировки ориентированного графа
 * (На множестве объектов задан частичный порядок, а не линейный)
 */
internal fun depthSort(objs: GameObjects) : List<Map.Entry<Long, GameObject>> {

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
    if (f(rect.leftX, rect.bottomY) <= 0 && f(rect.rightX, rect.topY) >= 0) {
        //Вычисляем другую функцию - функцию от точки p относительно прямой, соединяющей 2 угловые точки прямоугольника
        val fun2 = - p.y * rect.width + p.x * rect.height + rect.bottomY * rect.width - rect.leftX * rect.height
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
            return compare(Point(rectA.leftX, rectA.bottomY), rectB)
        }
        !aIsPoint && bIsPoint -> {
            if (rectA.overlaps(rectB)) return 1
            return -compare(Point(rectB.leftX, rectB.bottomY), rectA)
        }
        else -> return compareRectangles(rectA, rectB)
    }
}

internal fun compareEntityAndBuilding(entity: GameObject, building: GameObject) : Int {
    if (entity.type != GameObject.Type.ENTITY) Log.e("Entity should have ENTITY type")
    if (building.type != GameObject.Type.BUILDING) Log.e("Building should have BUILDING type")
    val rectA = entity.rectangle
    val rectB = building.rectangle
    val aIsPoint = rectA.width == 0f && rectA.height == 0f
    val bIsPoint = rectB.width == 0f && rectB.height == 0f
    if (rectA.overlaps(rectB)) return -1

    when {
        aIsPoint && bIsPoint -> {
            return -getVirtualScreenPointFromScene(entity.position).y.compareTo(getVirtualScreenPointFromScene(building.position).y)
        }
        aIsPoint && !bIsPoint -> {
            if (rectA.overlaps(rectB)) return -1
            return compare(Point(rectA.leftX, rectA.bottomY), rectB)
        }
        !aIsPoint && bIsPoint -> {
            if (rectA.overlaps(rectB)) return 1
            return -compare(Point(rectB.leftX, rectB.bottomY), rectA)
        }
        else -> return compareRectangles(rectA, rectB)
    }
}

private fun compareRectangles(rectA: Rectangle, rectB: Rectangle) : Int {
    var res = compare(Point(rectA.leftX, rectA.bottomY), rectB)
    if (res != 0) return res
    res = compare(Point(rectA.rightX, rectA.topY), rectB)
    if (res != 0) return res
    res = compare(Point(rectB.leftX, rectB.bottomY), rectA)
    if (res != 0) return -res
    res = compare(Point(rectB.rightX, rectB.topY), rectA)
    if (res != 0) return -res
    return 0
}

internal fun compare(a: GameObject, b: GameObject) : Int =
        if (a.type == GameObject.Type.ENTITY && b.type == GameObject.Type.BUILDING) compareEntityAndBuilding(a, b)
        else if (a.type == GameObject.Type.BUILDING && b.type == GameObject.Type.ENTITY) -compareEntityAndBuilding(b, a)
        else compareDisjoint(a, b)
