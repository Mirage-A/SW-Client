package com.mirage.utils.extensions

import com.badlogic.gdx.maps.Map
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.math.Rectangle
import com.mirage.utils.messaging.MoveDirection
import com.mirage.utils.datastructures.IntPair
import com.mirage.utils.datastructures.MutablePoint

operator fun Map.iterator() : Iterator<MapObject> {
    return object : Iterator<MapObject> {
        var i = 0
        var j = 0

        override fun hasNext(): Boolean {
            while (i < layers.count && j >= layers[i].objects.count) {
                ++i
                j = 0
            }
            return i < layers.count
        }

        override fun next(): MapObject {
            if (!hasNext()) {
                throw NoSuchElementException("There are no elements left")
            }
            ++j
            return layers[i].objects[j - 1]
        }

    }
}

var MapObject.type : String?
    get() = properties["type"] as? String
    set(newType) = properties.put("type", newType)



var MapObject.moveDirection: MoveDirection
    get() = MoveDirection.fromString(properties.getString("move-direction", "DOWN"))
    set(md) {
        properties.put("move-direction", md.toString())
    }

var MapObject.position : MutablePoint
    get() = MutablePoint(properties.getFloat("x", 0f), properties.getFloat("y", 0f))
    set(p) {
        properties.put("x", p.x)
        properties.put("y", p.y)
    }

val Rectangle.center
    get() = MutablePoint(x + width / 2, y + height / 2)

var MapObject.isMoving : Boolean
    get() = properties.getBoolean("is-moving", false)
    set(value) = properties.put("is-moving", value)

var MapObject.isRigid : Boolean
    get() = properties.getBoolean("rigid", false)
    set(value) = properties.put("rigid", value)

var MapObject.speed : Float
    get() = properties.getFloat("speed", 0f)
    set(value) = properties.put("speed", value)

var MapObject.rectangle : Rectangle
    get() = Rectangle(properties.getFloat("x"), properties.getFloat("y"),
                properties.getFloat("width"), properties.getFloat("height"))
    set(rect) {
        properties.put("x", rect.x)
        properties.put("y", rect.y)
        properties.put("width", rect.width)
        properties.put("height", rect.height)
    }

/**
 * Находит на карте объект с данным именем
 * Если такого объекта нет, возвращается null
 */
fun Map.findObject(name: String) : MapObject? {
    for (obj in this) {
        if (obj.name == name) return obj
    }
    return null
}

fun MapObject.clone() : MapObject {
    val obj = MapObject()
    obj.opacity = opacity
    obj.color = color
    obj.isVisible = isVisible
    obj.name = name
    for (key in properties.keys) {
        obj.properties[key] = properties[key]
    }
    return obj
}

operator fun MapProperties.set(key: String, value: Any) = put(key, value)

fun MapProperties.getInt(key: String, defaultValue: Int = 0) : Int =
        get<Int>(key, defaultValue, Int::class.java)

fun MapProperties.getString(key: String, defaultValue: String = "") : String =
        get<String>(key, defaultValue, String::class.java)

fun MapProperties.getBoolean(key: String, defaultValue: Boolean = false) : Boolean =
        get<Boolean>(key, defaultValue, Boolean::class.java)

fun MapProperties.getFloat(key: String, defaultValue: Float = 0f) : Float =
        get<Float>(key, defaultValue, Float::class.java)

operator fun Array<IntArray>.get(indices: IntPair): Int =
        this[indices.x][indices.y]

operator fun Array<BooleanArray>.get(indices: IntPair): Boolean =
        this[indices.x][indices.y]

operator fun Array<IntArray>.set(indices: IntPair, value: Int) {
    this[indices.x][indices.y] = value
}

operator fun Array<BooleanArray>.set(indices: IntPair, value: Boolean) {
    this[indices.x][indices.y] = value
}
