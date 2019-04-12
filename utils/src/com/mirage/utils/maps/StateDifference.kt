package com.mirage.utils.maps

/**
 * Изменяемая разность двух состояний сцены.
 * Может использоваться только в однопоточной среде.
 */
class StateDifference {

    val newObjects : MutableList<GameObject> = ArrayList()
    val removedObjects : MutableList<Long> = ArrayList()
    val entityDifferences : MutableMap<Long, EntityDifference> = HashMap()
    val buildingDifferences : MutableMap<Long, BuildingDifference> = HashMap()

    /**
     * Применяет эту разность к состоянию [origin], создавая новые такое состояние
     * с изменениями, хранящимися в данном объекте.
     */
    fun projectOn(origin: GameObjects) : GameObjects = origin.update(newObjects) {
        id, obj ->
        if (id in removedObjects) null
        else when (obj) {
            is Building -> buildingDifferences[id]?.projectOn(obj)
            is Entity -> entityDifferences[id]?.projectOn(obj)
            else -> null
        }
    }
}