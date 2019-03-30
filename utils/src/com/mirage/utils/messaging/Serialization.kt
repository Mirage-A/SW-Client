package com.mirage.utils.messaging

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.mirage.utils.MAP_OBJ_DLMTR
import com.mirage.utils.PROPS_DLMTR
import com.mirage.utils.datastructures.MutablePoint
import com.mirage.utils.extensions.set


//TODO
//TODO Оптимизация: Передавать не строку, а поток, и писать сразу примитивные типы без перевода в строку


/**
 * Сериализация объекта карты в строку
 * @see deserializeMapObject
 */
fun MapObject.serialize() : String = buildString {
    val isRect = this@serialize is RectangleMapObject
    append("$isRect$MAP_OBJ_DLMTR$name$MAP_OBJ_DLMTR$opacity$MAP_OBJ_DLMTR$isVisible$MAP_OBJ_DLMTR")
    append(properties.serialize())
}

/**
 * Десериализация объекта карты из строки
 * @throws Exception если строка некорректна
 * @see MapObject.serialize
 */
fun deserializeMapObject(str: String) : MapObject {
    val args = str.split(MAP_OBJ_DLMTR)
    val isRect = args[0].toBoolean()
    val obj : MapObject = if (isRect) RectangleMapObject() else MapObject()
    obj.name = args[1]
    obj.opacity = args[2].toFloat()
    obj.isVisible = args[3].toBoolean()
    deserializeMapProperties(args[4], obj.properties)
    return obj
}

/**
 * Сериализация объекта MapProperties в строку
 * @see deserializeMapProperties
 */
fun MapProperties.serialize() : String = buildString {
    var flag = false
    for (key in keys) {
        if (flag) append(PROPS_DLMTR)
        flag = true
        append(key)
        append(PROPS_DLMTR)
        append(when(get(key)) {
            is Int -> "I"
            is Float -> "F"
            is Boolean -> "B"
            else -> "S"
        })
        append(PROPS_DLMTR)
        append(get(key).toString())
    }
}

/**
 * Десериализация MapProperties из строки (заполняет props)
 * @throws Exception если строка некорректна
 * @see MapProperties.serialize
 */
fun deserializeMapProperties(str: String, props: MapProperties) {
    if (str.isNotBlank()) {
        val args = str.split(PROPS_DLMTR)
        for (i in 0 until args.size step 3) {
            props[args[i]] = when (args[i + 1]) {
                "I" -> args[i + 2].toInt()
                "F" -> args[i + 2].toFloat()
                "B" -> args[i + 2].toBoolean()
                else -> args[i + 2]
            }
        }
    }
}

fun PositionSnapshot.serialize() : String = buildString {
    var flag = false
    for ((id, pos) in positions) {
        if (flag) append(PROPS_DLMTR)
        flag = true
        append(id)
        append(PROPS_DLMTR)
        append(pos.x)
        append(PROPS_DLMTR)
        append(pos.y)
    }
    flag = false
    append(MAP_OBJ_DLMTR)
    for ((id, md) in moveDirections) {
        if (flag) append(PROPS_DLMTR)
        flag = true
        append(id)
        append(PROPS_DLMTR)
        append(md.toString())
    }
    flag = false
    append(MAP_OBJ_DLMTR)
    for ((id, moving) in isMoving) {
        if (flag) append(PROPS_DLMTR)
        flag = true
        append(id)
        append(PROPS_DLMTR)
        append(moving.toString())
    }
    append(MAP_OBJ_DLMTR)
    append(createdTimeMillis)
}

fun deserializePositionSnapshot(str: String) : PositionSnapshot {
    val args = str.split(MAP_OBJ_DLMTR)
    val positions = args[0].split(PROPS_DLMTR)
    val moveDirections = args[1].split(PROPS_DLMTR)
    val isMoving = args[2].split(PROPS_DLMTR)
    return PositionSnapshot(
            HashMap<Long, MutablePoint>().apply {
                if (args[0].isNotBlank()) {
                    for (i in 0 until positions.size step 3) {
                        put(positions[i].toLong(), MutablePoint(positions[i + 1].toFloat(), positions[i + 2].toFloat()))
                    }
                }
            },
            HashMap<Long, MoveDirection>().apply {
                if (args[1].isNotBlank()) {
                    for (i in 0 until moveDirections.size step 2) {
                        put(moveDirections[i].toLong(), MoveDirection.fromString(moveDirections[i + 1]))
                    }
                }
            },
            HashMap<Long, Boolean>().apply {
                if (args[2].isNotBlank()) {
                    for (i in 0 until isMoving.size step 2) {
                        put(isMoving[i].toLong(), isMoving[i + 1].toBoolean())
                    }
                }
            },
            args[3].toLong()
    )
}