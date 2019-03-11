package com.mirage.utils.extensions

import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.CoerceJavaToLua

fun tableOf(vararg args: Pair<String, Any?>) = LuaTable().apply {
    for ((key, value) in args) {
        set(key, CoerceJavaToLua.coerce(value))
    }
}

operator fun <K, V> Map<K, V>.get(key: K?) : V? {
    key ?: return null
    return get(key)
}