package com.mirage.utils.extensions

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.mirage.utils.Assets
import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.JsePlatform
import java.io.Reader
import java.util.*
import java.util.concurrent.ConcurrentHashMap

typealias QuestProgress = LinkedHashMap<String, Int>
typealias PlayerCreationListener = (playerID: Long) -> Unit
typealias SkillNames = List<String>
typealias EntityID = Long
typealias BuildingID = Long
typealias IntervalMillis = Long
typealias TimeMillis = Long
typealias GameMapName = String
typealias ReturnCode = Int

fun runScript(reader: Reader, args: LuaTable) {
    val globals = JsePlatform.standardGlobals()
    val chunk = globals.load(reader,"main")
    chunk.call(args)
}

fun tableOf(vararg args: Pair<String, Any?>) = LuaTable().apply {
    for ((key, value) in args) {
        set(key, CoerceJavaToLua.coerce(value))
    }
}

fun LuaTable.toStr(): String =
        "LuaTable(${this.keys().map { "$it: ${this[it]}" }.joinToString()})"

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

inline fun <reified T: Any> Gson.fromJson(json: JsonElement): T? = this.fromJson(json, T::class.java)
inline fun <reified T: Any> Gson.fromJson(reader: Reader): T? = this.fromJson(reader, T::class.java)
inline fun <reified T: Any> Gson.fromJson(str: String): T? = this.fromJson(str, T::class.java)