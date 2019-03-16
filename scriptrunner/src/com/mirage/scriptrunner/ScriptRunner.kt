package com.mirage.scriptrunner

import com.mirage.utils.Assets
import com.mirage.scriptrunner.client.ClientScriptActions
import com.mirage.scriptrunner.logic.LogicScriptActions
import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import org.luaj.vm2.lib.jse.JsePlatform

/**
 * Запускает скрипт c аргументом-таблицей args, добавляя аргумент actions
 */
fun runClientScript(name: String, args: LuaTable, clientActions: ClientScriptActions) {
    val globals = JsePlatform.standardGlobals()
    val chunk = globals.load(Assets.loadClientScript(name)?.readText() ?: "")
    args.set("actions", CoerceJavaToLua.coerce(clientActions))
    chunk.call(args)
}

/**
 * Запускает скрипт c аргументом-таблицей args, добавляя аргумент actions
 */
fun runLogicScript(name: String, args: LuaTable, logicActions: LogicScriptActions) {
    val globals = JsePlatform.standardGlobals()
    val chunk = globals.load(Assets.loadLogicScript(name)?.readText() ?: "")
    args.set("actions", CoerceJavaToLua.coerce(logicActions))
    chunk.call(args)
}