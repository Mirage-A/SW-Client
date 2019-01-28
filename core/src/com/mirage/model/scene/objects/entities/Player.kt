package com.mirage.model.scene.objects.entities

import com.mirage.model.datastructures.Point
import com.mirage.view.animation.WeaponType

class Player(position: Point = Point(), speed: Float = 1.4f, isMoving: Boolean = false, moveAngle: Float = 0f, var weaponType: WeaponType = WeaponType.UNARMED) :
        Entity(position, speed, isMoving, moveAngle) {
}