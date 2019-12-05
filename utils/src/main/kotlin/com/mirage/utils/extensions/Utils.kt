package com.mirage.utils.extensions

import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import java.util.*

fun tableOf(vararg args: Pair<String, Any?>) = LuaTable().apply {
    for ((key, value) in args) {
        set(key, CoerceJavaToLua.coerce(value))
    }
}

fun <E> NavigableSet<E>.second() : E? = try { higher(first()) } catch(ex: NoSuchElementException) { null }

fun <K, V> Map<K, V>.mutableCopy() : MutableMap<K, V> = HashMap<K, V>().apply {
    for ((key, value) in this@mutableCopy) {
        this[key] = value
    }
}

fun <T> treeSetOf(vararg args: T) : TreeSet<T> = TreeSet<T>().apply {
    for (arg in args) {
        add(arg)
    }
}

inline fun <K, V> mutableMap(size: Int, keyInit: (Int) -> K, valueInit: (Int) -> V): MutableMap<K, V> =
        HashMap<K, V>().apply {
            for (i in 0 until size) {
                this[keyInit(i)] = valueInit(i)
            }
        }