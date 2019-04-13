package com.mirage.utils.extensions

import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import java.util.*

fun tableOf(vararg args: Pair<String, Any?>) = LuaTable().apply {
    for ((key, value) in args) {
        set(key, CoerceJavaToLua.coerce(value))
    }
}

operator fun <K, V> Map<K, V>.get(key: K?) : V? {
    key ?: return null
    return get(key)
}

fun <E> NavigableSet<E>.second() : E? = try { higher(first()) } catch(ex: NoSuchElementException) { null }

fun <K, V> Map<K, V>.mutableCopy() : MutableMap<K, V> = HashMap<K, V>().apply {
    for ((key, value) in this@mutableCopy) {
        this[key] = value
    }
}