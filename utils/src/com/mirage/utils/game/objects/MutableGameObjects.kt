package com.mirage.utils.game.objects

import com.mirage.utils.game.states.StateDifference
import java.util.*
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

    fun add(newObject: GameObject) : Long = add(newObject.mutableCopy())

    /**
     * Удаляет объект
     */
    fun remove(removedObjectID: Long) {
        objects.remove(removedObjectID)
    }

    override fun iterator(): Iterator<Map.Entry<Long, MutableGameObject>> = objects.iterator()

    /**
     * Находит различия между этим объектом и объектом [initialObjects], переданным в конструктор.
     */
    fun findDifferenceWithOrigin() : StateDifference = StateDifference(
            newObjects = HashMap<Long, GameObject>().apply {
                for ((newID, newObj) in objects) {
                    if (!initialObjects.containsKey(newID)) {
                        this[newID] = newObj.saveChanges()
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
            changedObjects = HashMap<Long, GameObject>().apply {
                for ((id, originObj) in initialObjects) {
                    val newObj = objects[id]?.saveChanges() ?: continue
                    if (newObj != originObj) this[id] = newObj
                }
            }
    )

    /**
     * Создаёт неизменяемую копию этого состояния.
     */
    fun saveChanges() : GameObjects = GameObjects(
            objects = HashMap<Long, GameObject>().apply {
                for ((key, value) in objects) {
                    this[key] = value.saveChanges()
                }
            },
            nextID = nextID
    )
}