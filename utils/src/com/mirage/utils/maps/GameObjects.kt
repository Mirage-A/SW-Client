package com.mirage.utils.maps

/**
 * Неизменяемая коллекция объектов карты на одном слое.
 * @param objects Неизменяемый список, который используется для хранения объектов.
 * ЭТОТ СПИСОК НЕ ДОЛЖЕН ИЗМЕНЯТЬСЯ ИЗВНЕ ПОСЛЕ ИСПОЛЬЗОВАНИЯ КАК ПАРАМЕТРА!
 */
class GameObjects(private val objects: List<GameObject>) : List<GameObject> by objects {

    /**
     * Создаёт новый экземпляр GameObjects с некоторыми изменениями.
     * @param newObjects Объекты, которые должны быть добавлены в коллекцию.
     * @param map Функция, которая получает объект текущей коллекции и должна вернуть то, что будет перенесено в новую.
     * Если возвращается объект, то он добавляется в коллекцию вместо старого.
     * Если возвращается null, то старый объект не добавляется в новую коллекцию (удаление объекта).
     */
    fun update(newObjects: Collection<GameObject>, map: (GameObject) -> GameObject?) : GameObjects {
        val list = ArrayList(newObjects)
        for (obj in objects) {
            map(obj)?.let { list.add(it) }
        }
        return GameObjects(list)
    }

    override fun iterator(): Iterator<GameObject> = objects.iterator()

}