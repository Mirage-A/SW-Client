package com.mirage.utils.extensions

import com.mirage.utils.gameobjects.GameObject
import com.mirage.utils.gameobjects.GameObjects
import com.mirage.utils.gameobjects.ObjectDifference
import com.mirage.utils.maps.StateDifference
import java.util.*
import kotlin.collections.HashMap


/**
 * Находит [StateDifference] между этим состоянием и состоянием [other]
 */
fun GameObjects.findDifferenceWith(other: GameObjects) : StateDifference =
        StateDifference(
                newObjects = HashMap<Long, GameObject>().apply {
                    for ((id, obj) in this@findDifferenceWith) {
                        if (!other.objects.containsKey(id)) this[id] = obj
                    }
                },
                removedObjects = TreeSet<Long>().apply {
                    for ((id, _) in other) {
                        if (!this@findDifferenceWith.objects.containsKey(id)) this.add(id)
                    }
                },
                objectDifferences = HashMap<Long, ObjectDifference>().apply {
                    for ((id, newObj) in this@findDifferenceWith) {
                        val oldObj = other[id] ?: continue
                        this[id] = newObj.findDifferenceWith(oldObj)
                    }
                }
        )
