package com.mirage.model.scripts

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.MapObject
import com.mirage.controller.Platform
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.JsePlatform

/**
 * Запускает скрипт assets/scripts/$name.lua
 */
fun runScript(name: String, args: Map<String, Any?>) {
    val globals = JsePlatform.standardGlobals()
    val chunk = globals.load(Gdx.files.internal(Platform.ASSETS_PATH + "scripts/$name.lua").reader(), "$name.lua")
    chunk.call(LuaValue.tableOf().apply {
        for ((key, value) in args) {
            set(key, CoerceJavaToLua.coerce(value))
        }
        set("utils", CoerceJavaToLua.coerce(ScriptUtils))
    })
}