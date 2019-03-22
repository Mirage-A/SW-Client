package com.mirage.utils.messaging

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.mirage.utils.INNER_DLMTR
import com.mirage.utils.extensions.rectangle

/**
 * Сериализация объекта карты в строку//TODO
 */
fun MapObject.serialize() : String = buildString {
    val isRect = this@serialize is RectangleMapObject
    append("$isRect$INNER_DLMTR$name$INNER_DLMTR$opacity$INNER_DLMTR$isVisible$INNER_DLMTR$color$INNER_DLMTR")
    append(properties.serialize())
    append(INNER_DLMTR)
    if (isRect) {
        append(rectangle.serialize())
    }
}

fun MapProperties.serialize() : String = buildString {
//TODO
}

fun Rectangle.serialize() : String = buildString {
//TODO
}

fun PositionSnapshot.serialize() : String = ""//TODO