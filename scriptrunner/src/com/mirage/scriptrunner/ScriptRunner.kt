package com.mirage.scriptrunner

import com.badlogic.gdx.Gdx
import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.JsePlatform

/**
 * Запускает скрипт по пути path c аргументом-таблицей args
 */
fun runScript(path: String, args: LuaTable) {
    val globals = JsePlatform.standardGlobals()
    val chunk = globals.load(Gdx.files.internal(path).reader(), path)
    //args.set("utils", CoerceJavaToLua.coerce(ScriptUtils))
    chunk.call(args)
}

/**
 * Запускает скрипт по пути path с аргументом-словарем args
 */
fun runScript(name: String, args: Map<String, Any?>) {
    val table = LuaTable()
    for ((key, value) in args) {
        table.set(key, CoerceJavaToLua.coerce(value))
    }
    runScript(name, table)
}