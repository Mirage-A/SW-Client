package com.mirage.model.scene.objects

import com.mirage.model.datastructures.Point

abstract class Building(position: Point = Point(), var type: Int) : SceneObject(position)