package com.mirage.core.extensions

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.mirage.core.Log
import com.mirage.core.game.objects.properties.Equipment
import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.JsePlatform
import java.io.Reader
import java.util.*

typealias QuestProgress = LinkedHashMap<String, Int>
typealias PlayerCreationListener = (playerID: Long) -> Unit
typealias SkillNames = List<String>
typealias EntityID = Long
typealias BuildingID = Long
typealias IntervalMillis = Long
typealias TimeMillis = Long
typealias GameMapName = String
typealias ReturnCode = Int

data class PlayerCreationRequest(
        val playerName: String = "Player",
        val questProgress: QuestProgress = QuestProgress(),
        val equipment: Equipment,
        val onCreate: PlayerCreationListener
)

private val luaGlobals = JsePlatform.standardGlobals()

fun runScript(reader: Reader, args: LuaTable) {
    try {
        val chunk = luaGlobals.load(reader, "main")
        chunk.call(args)
    } catch (ex: Exception) {
        Log.e("Error while evaluating script with args ${args.toStr()}")
        ex.printStackTrace()
    }
}

fun tableOf(vararg args: Pair<String, Any?>) = LuaTable().apply {
    for ((key, value) in args) {
        set(key, CoerceJavaToLua.coerce(value))
    }
}

fun LuaTable.toStr(): String =
        "LuaTable(${this.keys().joinToString { "$it: ${this[it]}" }})"

fun <E> NavigableSet<E>.second(): E? = try {
    higher(first())
} catch (ex: NoSuchElementException) {
    null
}

fun <K, V> Map<K, V>.mutableCopy(): MutableMap<K, V> = HashMap<K, V>().apply {
    for ((key, value) in this@mutableCopy) {
        this[key] = value
    }
}

fun <T> treeSetOf(vararg args: T): TreeSet<T> = TreeSet<T>().apply {
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

inline fun <T : Any> Queue<T>.processNewItems(block: (T) -> Unit) {
    while (true) {
        val item = poll() ?: break
        block(item)
    }
}

inline fun <reified T : Any> Gson.fromJson(json: JsonElement): T? = this.fromJson(json, T::class.java)
inline fun <reified T : Any> Gson.fromJson(reader: Reader): T? = this.fromJson(reader, T::class.java)
inline fun <reified T : Any> Gson.fromJson(str: String): T? = this.fromJson(str, T::class.java)