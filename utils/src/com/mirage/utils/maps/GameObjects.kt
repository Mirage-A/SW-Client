package com.mirage.utils.maps

/**
 * Неизменяемый словарь объектов карты.
 * @param initialObjects Неизменяемый словарь, в котором будут храниться объекты.
 * @param nextID ID, большее максимального ID в словаре. Используется в методе [update] для выбора ID для новых объектов.
 */
class GameObjects(initialObjects: Map<Long, GameObject>, private val nextID: Long) : Iterable<Map.Entry<Long, GameObject>> {

    private val objects : Map<Long, GameObject> = HashMap<Long, GameObject>().apply {
        for ((id, obj) in initialObjects) {
            this[id] = obj
        }
    }

    /**
     * Создаёт новый экземпляр GameObjects с некоторыми изменениями.
     * @param newObjects Объекты, которые должны быть добавлены в коллекцию.
     * @param map Функция, которая получает объект текущей коллекции и его id и должна вернуть то, что будет перенесено в новую.
     * Если возвращается объект, то он добавляется в коллекцию вместо старого.
     * Если возвращается null, то старый объект не добавляется в новую коллекцию (удаление объекта).
     */
    fun update(newObjects: Collection<GameObject>, map: (Long, GameObject) -> GameObject?) : GameObjects {
        val newMap = HashMap<Long, GameObject>()
        for ((id, obj) in objects) {
            map(id, obj)?.let { newMap[id] = it }
        }
        var counter = nextID
        for (obj in newObjects) {
            newMap[counter] = obj
            ++counter
        }
        return GameObjects(newMap, counter)
    }

    override fun iterator(): Iterator<Map.Entry<Long, GameObject>> = objects.iterator()

}