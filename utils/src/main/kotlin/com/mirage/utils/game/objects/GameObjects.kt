package com.mirage.utils.game.objects

import com.mirage.utils.Log

/**
 * Неизменяемый словарь объектов карты.
 * @param objects Неизменяемый словарь, в котором будут храниться объекты.
 * @param nextID ID, большее максимального ID в словаре. Используется в методе [update] для выбора ID для новых объектов.
 */
data class GameObjects(val objects : Map<Long, GameObject>, val nextID: Long) : Iterable<Map.Entry<Long, GameObject>> {

    /**
     * Создаёт новый экземпляр GameObjects с некоторыми изменениями.
     * @param newObjects Объекты, которые должны быть добавлены в коллекцию.
     * @param map Функция, которая получает объект текущей коллекции и его id и должна вернуть то, что будет перенесено в новую.
     * Если возвращается объект, то он добавляется в коллекцию вместо старого.
     * Если возвращается null, то старый объект не добавляется в новую коллекцию (удаление объекта).
     */
    fun update(newObjects: Map<Long, GameObject>, map: (Long, GameObject) -> GameObject?) : GameObjects {
        val newMap = HashMap<Long, GameObject>()
        for ((id, obj) in objects) {
            map(id, obj)?.let { newMap[id] = it }
        }
        for ((id, obj) in newObjects) {
            if (newMap.containsKey(id)) Log.e("WARNING (GameObjects::update): new object's ID is already taken (maybe because of some desync between client and server, it's ok)")
            newMap[id] = obj
        }
        val newNextID = nextID + newObjects.size
        if (newMap.any {it.key == newNextID} ) Log.e("WARNING (GameObjects::update): Invalid object ID found (maybe because of some desync between client and server, it's ok)")
        return GameObjects(newMap, newNextID)
    }

    /**
     * Создаёт изменяемую копию
     */
    fun mutableCopy() : MutableGameObjects = MutableGameObjects(objects, nextID)

    override fun iterator(): Iterator<Map.Entry<Long, GameObject>> = objects.iterator()

    operator fun get(id: Long) = objects[id]


}