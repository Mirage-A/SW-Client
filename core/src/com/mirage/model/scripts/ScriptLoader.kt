package com.mirage.model.scripts

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.MapObject
import com.mirage.controller.Platform
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.JsePlatform

/**
 * Запускает скрипт assets/scripts/$name.lua
 */
fun runScript(name: String, args: LuaTable) {
    val globals = JsePlatform.standardGlobals()
    val chunk = globals.load(Gdx.files.internal(Platform.ASSETS_PATH + "scripts/$name.lua").reader(), "$name.lua")
    args.set("utils", CoerceJavaToLua.coerce(ScriptUtils))
    chunk.call(args)
}

fun runScript(name: String, args: Map<String, Any?>) {
    val table = LuaTable()
    for ((key, value) in args) {
        table.set(key, CoerceJavaToLua.coerce(value))
    }
    runScript(name, table)
}