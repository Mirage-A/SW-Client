package com.mirage.model.scene.objects.entities

import com.mirage.model.scene.Point
import com.mirage.view.scene.objects.humanoid.WeaponType

class Player(position: Point = Point(), speed: Float = 1f, isMoving: Boolean = false, moveAngle: Float = 0f, var weaponType: WeaponType = WeaponType.ONE_HANDED) :
        Entity(position, speed, isMoving, moveAngle) {
}