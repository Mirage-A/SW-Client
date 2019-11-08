package com.mirage.utils.game.states

import com.mirage.utils.game.objects.GameObject
import com.mirage.utils.game.objects.GameObjects
import java.util.*
import kotlin.collections.HashMap

/**
 * Неизменяемая разность двух состояний сцены.
 */
data class StateDifference(
        val newObjects : Map<Long, GameObject> = HashMap(),
        val removedObjects : Collection<Long> = TreeSet(),
        /**
         * Словарь с новыми версиями измененных объектов
         */
        val changedObjects : Map<Long, GameObject> = HashMap()
) {

    /**
     * Применяет эту разность к состоянию [origin], создавая новое такое состояние
     * с изменениями, хранящимися в данном объекте.
     */
    fun projectOn(origin: GameObjects) : GameObjects = origin.update(newObjects) {
        id, obj ->
        if (id in removedObjects) null
        else changedObjects[id] ?: obj
    }
}