package com.mirage.utils.gameobjects

import com.mirage.utils.maps.StateDifference
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MutableGameObjects(private val initialObjects: Map<Long, GameObject>, private var nextID: Long) : Iterable<Map.Entry<Long, MutableGameObject>> {

    private val objects: MutableMap<Long, MutableGameObject> = HashMap<Long, MutableGameObject>().apply {
        for ((id, obj) in initialObjects) {
            this[id] = obj.mutableCopy()
        }
    }

    /**
     * Позволяет получить объект по ID
     */
    operator fun get(id : Long) : MutableGameObject? = objects[id]

    /**
     * Добавляет объект и возвращает его ID
     */
    fun add(newObject: MutableGameObject) : Long {
        objects[nextID] = newObject
        ++nextID
        return nextID
    }

    /**
     * Удаляет объект
     */
    fun remove(removedObjectID: Long) {
        objects.remove(removedObjectID)
    }

    override fun iterator(): Iterator<Map.Entry<Long, MutableGameObject>> = objects.iterator()

    /**
     * Находит различия между этим объектом и объектом [initialObjects], переданным в конструктор.
     * Для создания объекта [StateDifference] используется список [newClientScripts] как один из параметров.
     */
    fun findDifferenceWithOrigin(newClientScripts: List<String> = ArrayList()) : StateDifference = StateDifference(
            newObjects = HashMap<Long, GameObject>().apply {
                for ((newID, newObj) in objects) {
                    if (!initialObjects.containsKey(newID)) {
                        this[newID] = newObj
                    }
                }
            },
            removedObjects = TreeSet<Long>().apply {
                for ((oldID, _) in initialObjects) {
                    if (!objects.containsKey(oldID)) {
                        this.add(oldID)
                    }
                }
            },
            objectDifferences = HashMap<Long, ObjectDifference>().apply {
                for ((id, originObj) in initialObjects) {
                    val newObj = objects[id] ?: continue
                    this[id] = newObj.findDifference(originObj)
                }
            },
            newClientScripts = newClientScripts
    )
}