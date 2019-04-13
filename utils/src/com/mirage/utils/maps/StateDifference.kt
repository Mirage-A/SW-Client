package com.mirage.utils.maps

/**
 * Изменяемая разность двух состояний сцены.
 * Может использоваться только в однопоточной среде.
 */
class StateDifference {

    val newObjects : MutableList<GameObject> = ArrayList()
    val removedObjects : MutableList<Long> = ArrayList()
    val objectDifferences : MutableMap<Long, ObjectDifference> = HashMap()
    val newClientScripts : MutableList<String> = ArrayList()

    /**
     * Применяет эту разность к состоянию [origin], создавая новые такое состояние
     * с изменениями, хранящимися в данном объекте.
     */
    fun projectOn(origin: GameObjects) : GameObjects = origin.update(newObjects) {
        id, obj ->
        if (id in removedObjects) null
        else objectDifferences[id]?.projectOn(obj)
    }
}