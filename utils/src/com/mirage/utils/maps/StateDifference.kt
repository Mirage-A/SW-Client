package com.mirage.utils.maps

import com.mirage.utils.gameobjects.GameObject
import com.mirage.utils.gameobjects.GameObjects
import com.mirage.utils.gameobjects.ObjectDifference
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Изменяемая разность двух состояний сцены.
 * Может использоваться только в однопоточной среде.
 */
data class StateDifference(
        val newObjects : List<GameObject> = ArrayList(),
        val removedObjects : Collection<Long> = TreeSet(),
        val objectDifferences : Map<Long, ObjectDifference> = HashMap(),
        val newClientScripts : List<String> = ArrayList()
) {


    /**
     * Применяет эту разность к состоянию [origin], создавая новые такое состояние
     * с изменениями, хранящимися в данном объекте.
     */
    fun projectOn(origin: GameObjects) : GameObjects = origin.update(newObjects) {
        id, obj ->
        if (id in removedObjects) null
        else objectDifferences[id]?.projectOn(obj) ?: obj
    }
}