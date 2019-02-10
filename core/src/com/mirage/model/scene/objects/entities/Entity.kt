package com.mirage.model.scene.objects.entities

import com.mirage.model.datastructures.Point
import com.mirage.model.scene.objects.SceneObject

abstract class Entity(position: Point = Point(), var speed: Float = 2.8f, var isMoving: Boolean = false, var moveAngle: Float = 0f) :
        SceneObject(position)