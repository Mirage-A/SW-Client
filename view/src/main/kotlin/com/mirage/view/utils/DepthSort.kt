package com.mirage.view.utils

import com.mirage.core.utils.Point
import com.mirage.core.utils.Rectangle
import com.mirage.core.game.objects.SimplifiedBuilding
import com.mirage.core.game.objects.SimplifiedEntity
import com.mirage.core.game.objects.SimplifiedObject
import java.util.*


/**
 * Sorts list of game objects in their rendering order.
 * This method uses graph topological sorting algorithm, since "rendering" order is not linear.
 */
internal fun depthSort(objsList: MutableList<Pair<Long, SimplifiedObject>>) {

    val q = ArrayDeque<Pair<Long, SimplifiedObject>>()
    val visited = BooleanArray(objsList.size) { false }
    fun dfs(index: Int) {
        if (visited[index]) return
        visited[index] = true
        for (i in 0 until objsList.size) {
            if (compare(objsList[index].second, objsList[i].second) == 1) {
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
}

/**
 * Returns 1, if [p] must be rendered after [rect]
 * Returns -1, if [p] must be rendered before [rect]
 * Returns 0, if [p] and [rect] may be rendered in any relative order
 */
private fun compare(p: Point, rect: Rectangle): Int {
    /** This function computes position of point (x, y) relative to diagonal line, passing through [p] */
    fun f(x: Float, y: Float): Float = x + y - p.x - p.y

    //If line and rectangle intersect
    if (f(rect.leftX, rect.bottomY) <= 0 && f(rect.rightX, rect.topY) >= 0) {
        //Then compute function from point p relative to line that contains [rect] diagonal
        val fun2 = -p.y * rect.width + p.x * rect.height + rect.bottomY * rect.width - rect.leftX * rect.height
        if (fun2 > 0) return 1
        if (fun2 < 0) return -1
    }
    return 0
}

/**
 * Returns 1, if [a] must be rendered after [b]
 * Returns -1, if [a] must be rendered before [b]
 * Returns 0, if [a] and [b] may be rendered in any relative order
 */
private fun compareDisjoint(a: SimplifiedObject, b: SimplifiedObject): Int {
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

internal fun compareEntityAndBuilding(entity: SimplifiedEntity, building: SimplifiedBuilding): Int {
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

private fun compareRectangles(rectA: Rectangle, rectB: Rectangle): Int {
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

internal fun compare(a: SimplifiedObject, b: SimplifiedObject): Int =
        if (a is SimplifiedEntity && b is SimplifiedBuilding) compareEntityAndBuilding(a, b)
        else if (a is SimplifiedBuilding && b is SimplifiedEntity) -compareEntityAndBuilding(b, a)
        else compareDisjoint(a, b)
