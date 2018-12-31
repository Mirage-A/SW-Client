package com.mirage.model.scene.objects.entities

import com.mirage.model.scene.Point

class Player(position: Point = Point(), speed: Float = 1f, isMoving: Boolean = false, moveAngle: Float = 0f) :
        Entity(position, speed, isMoving, moveAngle) {
}