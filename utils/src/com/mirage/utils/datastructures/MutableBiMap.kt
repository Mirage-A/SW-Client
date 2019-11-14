package com.mirage.utils.datastructures

/**
 * Аналог MutableMap для двух аргументов
 */
class MutableBiMap<T1, T2, R> {

    private val map : MutableMap<T1, MutableMap<T2, R>> = HashMap()

    var size: Int = 0
        private set

    fun containsKey(key1: T1, key2: T2): Boolean = this[key1, key2] != null

    fun containsValue(value: R): Boolean = map.values.any {it.containsValue(value)}

    operator fun get(key1: T1, key2: T2): R? = map[key1]?.get(key2)

    fun isEmpty(): Boolean = size == 0

    fun clear() {
        map.clear()
        size = 0
    }

    operator fun set(key1: T1, key2: T2, value: R) {
        if (!map.containsKey(key1)) map[key1] = HashMap()
        val subMap : MutableMap<T2, R> = map[key1] ?: HashMap()
        if (!subMap.containsKey(key2)) ++size
        subMap[key2] = value
    }

    fun remove(key1: T1, key2: T2): R? {
        if (!map.containsKey(key1)) return null
        val subMap : MutableMap<T2, R> = map[key1] ?: HashMap()
        if (subMap.containsKey(key2)) --size
        return subMap.remove(key2)
    }

}